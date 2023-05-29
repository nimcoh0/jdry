/**
 *
 */
package org.softauto.podam;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softauto.espl.Espl;
import uk.co.jemos.podam.api.*;
import uk.co.jemos.podam.api.DataProviderStrategy.Order;
import uk.co.jemos.podam.common.AttributeStrategy;
import uk.co.jemos.podam.common.ManufacturingContext;
import uk.co.jemos.podam.common.PodamConstants;
import uk.co.jemos.podam.common.PodamConstructor;
import uk.co.jemos.podam.exceptions.PodamMockeryException;

import javax.xml.ws.Holder;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;


/**
 * The PODAM factory implementation
 *
 * @author mtedone
 *
 * @since 1.0.0
 *
 */
@NotThreadSafe
@Immutable
public class ExtendPodamFactoryImpl implements PodamFactory {

	// ------------------->> Constants

	private static final String RESOLVING_COLLECTION_EXCEPTION_STR = "An exception occurred while resolving the collection";

	private static final String MAP_CREATION_EXCEPTION_STR = "An exception occurred while creating a Map object";

    /** Application logger */
	private static final Logger LOG = LoggerFactory.getLogger(ExtendPodamFactoryImpl.class);


	// ------------------->> Instance / variables

	/**
	 * External factory to delegate production this factory cannot handle
	 * <p>
	 * The default is {@link NullExternalFactory}.
	 * </p>
	 */
	private PodamFactory externalFactory
			= NullExternalFactory.getInstance();

	/**
	 * The strategy to use to fill data.
	 * <p>
	 * The default is {@link RandomDataProviderStrategyImpl}.
	 * </p>
	 */
	private DataProviderStrategy strategy
			= new RandomDataProviderStrategyImpl();

	/**
	 * The strategy to use to introspect data.
	 * <p>
	 * The default is {@link ExtendDefaultClassInfoStrategy}.
	 * </p>
	 */
	private ClassInfoStrategy classInfoStrategy
			= ExtendDefaultClassInfoStrategy.getInstance();

	// ------------------->> Constructors

	/**
	 * Default constructor.
	 */
	public ExtendPodamFactoryImpl() {
		this(NullExternalFactory.getInstance(),
				new RandomDataProviderStrategyImpl());
	}


	/**
	 * Constructor with non-default strategy
	 *
	 * @param strategy
	 *            The strategy to use to fill data
	 */
	public ExtendPodamFactoryImpl(DataProviderStrategy strategy) {

		this(NullExternalFactory.getInstance(), strategy);
	}

	/**
	 * Constructor with non-default external factory
	 *
	 * @param externalFactory
	 *            External factory to delegate production this factory cannot
	 *            handle
	 */
	public ExtendPodamFactoryImpl(PodamFactory externalFactory) {
		this(externalFactory, new RandomDataProviderStrategyImpl());
	}

	/**
	 * Full constructor.
	 *
	 * @param externalFactory
	 *            External factory to delegate production this factory cannot
	 *            handle
	 * @param strategy
	 *            The strategy to use to fill data
	 */
	public ExtendPodamFactoryImpl(PodamFactory externalFactory,
								  DataProviderStrategy strategy) {
		this.externalFactory = externalFactory;
		this.strategy = strategy;
	}

	// ------------------->> Public methods

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T manufacturePojoWithFullData(Class<T> pojoClass, Type... genericTypeArgs) {
		ManufacturingContext manufacturingCtx = new ManufacturingContext();
		manufacturingCtx.getPojos().put(pojoClass, 0);
		manufacturingCtx.setConstructorOrdering(Order.HEAVY_FIRST);
		return doManufacturePojo(pojoClass, manufacturingCtx, genericTypeArgs);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T manufacturePojo(Class<T> pojoClass, Type... genericTypeArgs) {
		ManufacturingContext manufacturingCtx = new ManufacturingContext();
		manufacturingCtx.getPojos().put(pojoClass, 0);
		return doManufacturePojo(pojoClass, manufacturingCtx, genericTypeArgs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T populatePojo(T pojo, Type... genericTypeArgs) {
		ManufacturingContext manufacturingCtx = new ManufacturingContext();
		manufacturingCtx.getPojos().put(pojo.getClass(), 0);
		final Map<String, Type> typeArgsMap = new HashMap<String, Type>();
		Type[] genericTypeArgsExtra = TypeManufacturerUtil.fillTypeArgMap(typeArgsMap,
                pojo.getClass(), genericTypeArgs);
		try {
			return this.populatePojoInternal(pojo, manufacturingCtx, typeArgsMap,
					genericTypeArgsExtra);
		} catch (InstantiationException e) {
			throw new PodamMockeryException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new PodamMockeryException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new PodamMockeryException(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new PodamMockeryException(e.getMessage(), e);
		}
	}

	// ------------------->> Getters / Setters

	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataProviderStrategy getStrategy() {
		return strategy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PodamFactory setStrategy(DataProviderStrategy strategy) {
		this.strategy = strategy;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ClassInfoStrategy getClassStrategy() {
		return classInfoStrategy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PodamFactory setClassStrategy(ClassInfoStrategy classInfoStrategy) {
		this.classInfoStrategy = classInfoStrategy;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PodamFactory getExternalFactory() {
		return externalFactory;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PodamFactory setExternalFactory(PodamFactory externalFactory) {
		this.externalFactory = externalFactory;
		return this;
	}

	// ------------------->> Private methods

	/**
	 * It attempts to create an instance of the given class with a static method
	 * of the factory
	 * <p>
	 * This method attempts to instantiate POJO with a static method of provided
	 * factory, for example, getInstance().
	 * </p>
	 *
	 * @param <T>
	 *            The type of Pojo class
	 * @param factoryClass
	 *            The factory class, which will be used for POJO instantiation
	 * @param pojoClass
	 *            The name of the class for which an instance filled with values
	 * @param manufacturingCtx
	 *            the manufacturing context
	 * @param typeArgsMap
	 *            a map relating the generic class arguments ("&lt;T, V&gt;" for
	 *            example) with their actual types
	 * @param genericTypeArgs
	 *            The generic type arguments for the current generic class
	 *            instance
	 *
	 *
	 * @return An instance of the given class
	 * @throws IllegalArgumentException
	 *             If an illegal argument was passed to the constructor
	 * @throws InstantiationException
	 *             If an exception occurred during instantiation
	 * @throws IllegalAccessException
	 *             If security was violated while creating the object
	 * @throws InvocationTargetException
	 *             If an exception occurred while invoking the constructor or
	 *             factory method
	 * @throws ClassNotFoundException
	 *             If it was not possible to create a class from a string
	 */
	private <T> T instantiatePojoWithFactory(
			Class<?> factoryClass, Class<T> pojoClass,
			ManufacturingContext manufacturingCtx,
			Map<String, Type> typeArgsMap, Type... genericTypeArgs)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {

		// If no publicly accessible constructors are available,
		// the best we can do is to find a constructor (e.g.
		// getInstance())

		Method[] declaredMethods = factoryClass.getDeclaredMethods();
		strategy.sort(declaredMethods, manufacturingCtx.getConstructorOrdering());

		// A candidate factory method is a method which returns the
		// Class type

		// The parameters to pass to the method invocation
		Object[] parameterValues = null;

		for (Method candidateConstructor : declaredMethods) {

			if (!candidateConstructor.getReturnType().equals(pojoClass)) {
				continue;
			}

			Object factoryInstance = null;
			if (!Modifier.isStatic(candidateConstructor.getModifiers())) {
				if (factoryClass.equals(pojoClass)) {
					continue;
				} else {
					factoryInstance = manufacturePojo(factoryClass);
				}
			}

			parameterValues = getParameterValuesForMethod(candidateConstructor,
					pojoClass, manufacturingCtx, typeArgsMap, genericTypeArgs);

			try {

				@SuppressWarnings("unchecked")
				T retValue = (T) candidateConstructor.invoke(factoryInstance,
						parameterValues);
				LOG.debug("Could create an instance using "
						+ candidateConstructor);

				if (retValue != null) {
					return retValue;
				}

			} catch (Exception t) {

				LOG.debug(
						"PODAM could not create an instance for constructor: "
								+ candidateConstructor
								+ ". Will try another one...", t);

			}

		}

		LOG.debug("For class {} PODAM could not possibly create"
				+ " a value statically. Will try other means.",
				pojoClass);
		return null;

	}

    /**
	 * It creates and returns an instance of the given class if at least one of
	 * its constructors has been annotated with {@link PodamConstructor}
	 *
	 * @param <T>
	 *            The type of the instance to return
	 *
	 * @param pojoClass
	 *            The class of which an instance is required
	 * @param manufacturingCtx
	 *            the manufacturing context
	 * @param typeArgsMap
	 *            a map relating the generic class arguments ("&lt;T, V&gt;" for
	 *            example) with their actual types
	 * @param genericTypeArgs
	 *            The generic type arguments for the current generic class
	 *            instance
	 * @return an instance of the given class if at least one of its
	 *         constructors has been annotated with {@link PodamConstructor}
	 * @throws SecurityException
	 *             If an security was violated
	 */
	private <T> T instantiatePojo(Class<T> pojoClass,
			ManufacturingContext manufacturingCtx, Map<String, Type> typeArgsMap,
			Type... genericTypeArgs)
			throws SecurityException {

		T retValue = null;

		Constructor<?>[] constructors = pojoClass.getConstructors();
		if (constructors.length == 0 || Modifier.isAbstract(pojoClass.getModifiers())) {
			/* No public constructors, we will try static factory methods */
			try {
				retValue = instantiatePojoWithFactory(
						pojoClass, pojoClass, manufacturingCtx, typeArgsMap, genericTypeArgs);
			} catch (Exception e) {
				LOG.debug("We couldn't create an instance for pojo: "
						+ pojoClass + " with factory methods, will "
						+ " try non-public constructors.", e);
			}

			/* Then non-public constructors */
			if (retValue == null) {
				constructors = pojoClass.getDeclaredConstructors();
			}
		}

		if (retValue == null && constructors.length > 0) {

			strategy.sort(constructors, manufacturingCtx.getConstructorOrdering());

			for (Constructor<?> constructor : constructors) {

				try {
					Object[] parameterValues = getParameterValuesForConstructor(
							constructor, pojoClass, manufacturingCtx, typeArgsMap,
							genericTypeArgs);

					// Security hack
					if (!constructor.isAccessible()) {
						constructor.setAccessible(true);
					}

					@SuppressWarnings("unchecked")
					T tmp = (T) constructor.newInstance(parameterValues);
					retValue = tmp;
					if (retValue != null) {
						LOG.debug("We could create an instance with constructor: "
								+ constructor);
						break;
					}
				} catch (Exception e) {
					LOG.debug("We couldn't create an instance for pojo: {} with"
							+ " constructor: {}. Will try with another one.",
							pojoClass, constructor, e);
				}
			}
		}

		if (retValue == null) {
			LOG.debug("For class {} PODAM could not possibly create"
					+ " a value. Will try other means.", pojoClass);
		}
		return retValue;
	}

	/**
	 * Manufactures and populates the pojo class
	 *
	 * @param <T> The type of the instance to return
	 * @param pojoClass the class to instantiate
	 * @param manufacturingCtx the initialized manufacturing context
	 * @param genericTypeArgs generic arguments for the pojo class
	 * @return instance of @pojoClass or null in case it cannot be instantiated
	 */
	private <T> T doManufacturePojo(Class<T> pojoClass,
			ManufacturingContext manufacturingCtx, Type... genericTypeArgs) {
		try {
			Class<?> declaringClass = null;
			Object declaringInstance = null;
			AttributeMetadata pojoMetadata = new AttributeMetadata(pojoClass,
					pojoClass, genericTypeArgs, declaringClass, declaringInstance);
			return this.manufacturePojoInternal(pojoClass, pojoMetadata,
                    manufacturingCtx, genericTypeArgs);
		} catch (InstantiationException e) {
			throw new PodamMockeryException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new PodamMockeryException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new PodamMockeryException(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new PodamMockeryException(e.getMessage(), e);
		}
	}

	/**
	 * Generic method which returns an instance of the given class filled with
	 * values dictated by the strategy
	 *
	 * @param <T>
	 *            The type for which a filled instance is required
	 *
	 * @param pojoClass
	 *            The name of the class for which an instance filled with values
	 *            is required
	 * @param pojoMetadata
	 *            attribute metadata for POJOs produced recursively
	 * @param manufacturingCtx
	 *            the manufacturing context
	 * @param genericTypeArgs
	 *            The generic type arguments for the current generic class
	 *            instance
	 * @return An instance of &lt;T&gt; filled with dummy values
	 * @throws InstantiationException
	 *             If an exception occurred during instantiation
	 * @throws IllegalAccessException
	 *             If security was violated while creating the object
	 * @throws InvocationTargetException
	 *             If an exception occurred while invoking the constructor or
	 *             factory method
	 * @throws ClassNotFoundException
	 *             If manufactured class cannot be loaded
	 * @throws PodamMockeryException
	 *             if a problem occurred while creating a POJO instance or while
	 *             setting its state
	 */
	private <T> T manufacturePojoInternal(Class<T> pojoClass,
			AttributeMetadata pojoMetadata, ManufacturingContext manufacturingCtx,
			Type... genericTypeArgs)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {

		// reuse object from memoization table
		@SuppressWarnings("unchecked")
		T objectToReuse = (T) strategy.getMemoizedObject(pojoMetadata);
		if (objectToReuse != null) {
			LOG.debug("Fetched memoized object for {} with parameters {}",
					pojoClass, Arrays.toString(genericTypeArgs));
			return objectToReuse;
		} else {
			LOG.debug("Manufacturing {} with parameters {}",
					pojoClass, Arrays.toString(genericTypeArgs));
		}

		final Map<String, Type> typeArgsMap = new HashMap<String, Type>();
		Type[] genericTypeArgsExtra = TypeManufacturerUtil.fillTypeArgMap(typeArgsMap,
				pojoClass, genericTypeArgs);

		T retValue = (T) strategy.getTypeValue(pojoMetadata, typeArgsMap, pojoClass);
		if (null == retValue) {

			if (pojoClass.isInterface()) {

				return getValueForAbstractType(pojoClass, pojoMetadata,
						manufacturingCtx, typeArgsMap, genericTypeArgs);
			}

			try {

				retValue = instantiatePojo(pojoClass, manufacturingCtx, typeArgsMap,
						genericTypeArgsExtra);
			} catch (SecurityException e) {

				throw new PodamMockeryException(
						"Security exception while applying introspection.", e);
			}
		}

		if (retValue == null) {
			return getValueForAbstractType(pojoClass, pojoMetadata,
					manufacturingCtx, typeArgsMap, genericTypeArgs);
		} else {

			// update memoization cache with new object
			// the reference is stored before properties are set so that recursive
			// properties can use it
			strategy.cacheMemoizedObject(pojoMetadata, retValue);

			populatePojoInternal(retValue, manufacturingCtx, typeArgsMap, genericTypeArgsExtra);
		}

		return retValue;
	}



    /**
	 * Fills given class filled with values dictated by the strategy
	 *
	 * @param <T>
	 *            The type for which should be populated
	 * @param pojo
	 *            An instance to be filled with dummy values
	 * @param manufacturingCtx
	 *            the manufacturing context
	 * @param typeArgsMap
	 *            a map relating the generic class arguments ("&lt;T, V&gt;" for
	 *            example) with their actual types
	 * @param genericTypeArgs
	 *            The generic type arguments for the current generic class
	 *            instance
	 * @return An instance of &lt;T&gt; filled with dummy values
	 * @throws InstantiationException
	 *             If an exception occurred during instantiation
	 * @throws IllegalAccessException
	 *             If security was violated while creating the object
	 * @throws InvocationTargetException
	 *             If an exception occurred while invoking the constructor or
	 *             factory method
	 * @throws ClassNotFoundException
	 *             If manufactured class cannot be loaded
	 */
	private <T> T populatePojoInternal(T pojo, ManufacturingContext manufacturingCtx,
			Map<String, Type> typeArgsMap,
			Type... genericTypeArgs)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {

		Class<?> pojoClass = pojo.getClass();
		if (pojo instanceof Collection && ((Collection<?>)pojo).isEmpty()) {
			@SuppressWarnings("unchecked")
			Collection<Object> collection = (Collection<Object>) pojo;
			AtomicReference<Type[]> elementGenericTypeArgs = new AtomicReference<Type[]>(
					PodamConstants.NO_TYPES);
			Class<?> elementTypeClass = findInheretedCollectionElementType(collection,
					manufacturingCtx, elementGenericTypeArgs, typeArgsMap, genericTypeArgs);
			String attributeName = null;
			Annotation[] annotations = collection.getClass().getAnnotations();
			fillCollection(manufacturingCtx, Arrays.asList(annotations), attributeName,
					collection, elementTypeClass, elementGenericTypeArgs.get());
		} else if (pojo instanceof Map && ((Map<?,?>)pojo).isEmpty()) {
			@SuppressWarnings("unchecked")
			Map<Object,Object> map = (Map<Object,Object>)pojo;
			MapArguments mapArguments = findInheretedMapElementType(
					map, manufacturingCtx, typeArgsMap, genericTypeArgs);
			fillMap(mapArguments, manufacturingCtx);
		}

		Class<?>[] parameterTypes = null;
		Class<?> attributeType = null;

		ClassInfo classInfo = classInfoStrategy.getClassInfo(pojo.getClass());

		Set<ClassAttribute> classAttributes = classInfo.getClassAttributes();

		// According to JavaBeans standards, setters should have only
		// one argument
		Object setterArg = null;
		Iterator<ClassAttribute> iter = classAttributes.iterator();
		while (iter.hasNext()) {

			ClassAttribute attribute = iter.next();
			Set<Method> setters = attribute.getSetters();
			if (setters.isEmpty()) {
				if (attribute.getGetters().isEmpty()) {
					iter.remove();
				}
				continue;
			} else {
				iter.remove();
			}

			/* We want to find setter defined the latest */
			Method setter = null;
			for (Method current : setters) {
				if (setter == null || setter.getDeclaringClass().isAssignableFrom(current.getDeclaringClass())) {
					setter = current;
				}
			}

			parameterTypes = setter.getParameterTypes();
			if (parameterTypes.length != 1) {
				LOG.warn("Skipping setter with non-single arguments {}",
						setter);
				continue;
			}

			// A class which has got an attribute to itself (e.g.
			// recursive hierarchies)
			attributeType = parameterTypes[0];

			// If an attribute has been annotated with
			// PodamAttributeStrategy, it takes the precedence over any
			// other strategy. Additionally we don't pass the attribute
			// metadata for value customisation; if user went to the extent
			// of specifying a PodamAttributeStrategy annotation for an
			// attribute they are already customising the value assigned to
			// that attribute.

			List<Annotation> pojoAttributeAnnotations
					= PodamUtils.getAttributeAnnotations(
							attribute.getAttribute(), setter);


			AttributeStrategy<?> attributeStrategy
					= TypeManufacturerUtil.findAttributeStrategy(strategy, pojoAttributeAnnotations, attributeType);
			if (null != attributeStrategy) {

				LOG.debug("The attribute: " + attribute.getName()
						+ " will be filled using the following strategy: "
						+ attributeStrategy);

				setterArg = TypeManufacturerUtil.returnAttributeDataStrategyValue(attributeType,
                        attributeStrategy);

			} else {

				AtomicReference<Type[]> typeGenericTypeArgs
						= new AtomicReference<Type[]>(PodamConstants.NO_TYPES);
				// If the parameter is a generic parameterized type resolve
				// the actual type arguments
				Type genericType = setter.getGenericParameterTypes()[0];

				final Type[] typeArguments;
				if (!(genericType instanceof GenericArrayType)) {
					attributeType = TypeManufacturerUtil.resolveGenericParameter(genericType,
							typeArgsMap, typeGenericTypeArgs);
					typeArguments = typeGenericTypeArgs.get();
				} else {
					typeArguments = PodamConstants.NO_TYPES;
				}

				for (int i = 0; i < typeArguments.length; i++) {
					if (typeArguments[i] instanceof TypeVariable) {
						Class<?> resolvedType = TypeManufacturerUtil.resolveGenericParameter(typeArguments[i],
                                typeArgsMap, typeGenericTypeArgs);
						if (!Collection.class.isAssignableFrom(resolvedType) && !Map.class.isAssignableFrom(resolvedType)) {
							typeArguments[i] = resolvedType;
						}
					}
				}

				setterArg = manufactureAttributeValue(pojo, manufacturingCtx,
						attributeType, genericType,
						pojoAttributeAnnotations, attribute.getName(),
						typeArgsMap, typeArguments);
			}

			try {
				setter.invoke(pojo, setterArg);
			} catch(IllegalAccessException e) {
				LOG.warn("{} is not accessible. Setting it to accessible."
						+ " However this is a security hack and your code"
						+ " should really adhere to JavaBeans standards.",
						setter.toString());
				setter.setAccessible(true);
				setter.invoke(pojo, setterArg);
			}
		}

		for (ClassAttribute readOnlyAttribute : classAttributes) {

			Method getter = readOnlyAttribute.getGetters().iterator().next();
			if (getter != null && !getter.getReturnType().isPrimitive()) {

				if (getter.getGenericParameterTypes().length == 0) {

					Object fieldValue = null;
					try {
						fieldValue = getter.invoke(pojo, PodamConstants.NO_ARGS);
					} catch(Exception e) {
						LOG.debug("Cannot access {}, skipping", getter);
					}
					if (fieldValue != null) {

						LOG.debug("Populating read-only field {}", getter);
						Type[] genericTypeArgsAll;
						Map<String, Type> paramTypeArgsMap;
						if (getter.getGenericReturnType() instanceof ParameterizedType) {

							paramTypeArgsMap = new HashMap<String, Type>(typeArgsMap);

							ParameterizedType paramType
									= (ParameterizedType) getter.getGenericReturnType();
							Type[] actualTypes = paramType.getActualTypeArguments();
							TypeManufacturerUtil.fillTypeArgMap(paramTypeArgsMap,
									getter.getReturnType(), actualTypes);
							genericTypeArgsAll = TypeManufacturerUtil.fillTypeArgMap(paramTypeArgsMap,
                                    getter.getReturnType(), genericTypeArgs);

						} else {

							paramTypeArgsMap = typeArgsMap;
							genericTypeArgsAll = genericTypeArgs;
						}

						Class<?> fieldClass = fieldValue.getClass();
						Integer depth = manufacturingCtx.getPojos().get(fieldClass);
						if (depth == null) {
							depth = -1;
						}
						if (depth <= strategy.getMaxDepth(fieldClass)) {

							manufacturingCtx.getPojos().put(fieldClass, depth + 1);
							populatePojoInternal(fieldValue, manufacturingCtx, paramTypeArgsMap, genericTypeArgsAll);
							manufacturingCtx.getPojos().put(fieldClass, depth);

						} else {

							LOG.warn("Loop in filling read-only field {} detected.",
									getter);
						}
					}
				} else {

					LOG.warn("Skipping invalid getter {}", getter);
				}
			}
		}

		// It executes any extra methods
		Collection<Method> extraMethods = classInfoStrategy.getExtraMethods(pojoClass);
		if (null != extraMethods) {
			for (Method extraMethod : extraMethods) {

				Object[] args = getParameterValuesForMethod(extraMethod, pojoClass,
						manufacturingCtx, typeArgsMap, genericTypeArgs);
				extraMethod.invoke(pojo, args);
			}
		}

		return pojo;
	}

	HashMap<String,String> attributeValueMap = new HashMap<>();

	public ExtendPodamFactoryImpl setAttributeValueMap(HashMap<String, String> attributeValueMap) {
		this.attributeValueMap = attributeValueMap;
		return this;
	}

	/**
	 * It manufactures and returns the value for a POJO attribute.
	 *
	 *
	 * @param pojo
	 *            The POJO being filled with values
	 * @param manufacturingCtx
	 *            the manufacturing context
	 * @param attributeType
	 *            The type of the attribute for which a value is being
	 *            manufactured
	 * @param genericAttributeType
	 *            The generic type of the attribute for which a value is being
	 *            manufactured
	 * @param annotations
	 *            The annotations for the attribute being considered
	 * @param attributeName
	 *            The attribute name
	 * @param typeArgsMap
	 *            a map relating the generic class arguments ("&lt;T, V&gt;" for
	 *            example) with their actual types
	 * @param genericTypeArgs
	 *            The generic type arguments for the current generic class
	 *            instance
	 * @return The value for an attribute
	 *
	 * @throws InstantiationException
	 *             If an exception occurred during instantiation
	 * @throws IllegalAccessException
	 *             If security was violated while creating the object
	 * @throws InvocationTargetException
	 *             If an exception occurred while invoking the constructor or
	 *             factory method
	 * @throws IllegalArgumentException
	 *             <ul>
	 *             <li>If an illegal argument was passed</li>
	 *             <li>If an invalid value was set for a precise value in an
	 *             annotation and such value could not be converted to the
	 *             desired type</li>
	 *             </ul>
	 * @throws ClassNotFoundException
	 *              If class being manufactured cannot be loaded
	 *
	 */
	private Object manufactureAttributeValue(Object pojo,
			ManufacturingContext manufacturingCtx, Class<?> attributeType,
			Type genericAttributeType, List<Annotation> annotations,
			String attributeName, Map<String, Type> typeArgsMap,
			Type... genericTypeArgs)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {

		Object attributeValue = null;

		Class<?> pojoClass = (pojo instanceof Class ? (Class<?>) pojo : pojo.getClass());
		Class<?> realAttributeType;

		if(attributeValueMap != null && attributeValueMap.size()>0) {
			for (Map.Entry entry : attributeValueMap.entrySet()) {
				String key = entry.getKey().toString();
				String pkey = pojoClass.getName() + "." + attributeName;
				if (key.equals(pkey)) {
					Object o = Espl.getInstance().evaluate(entry.getValue().toString());
					return attributeValue = o;
				}
			}
		}


		if (attributeType != genericAttributeType
				&& Object.class.equals(attributeType)
				&& genericAttributeType instanceof TypeVariable) {
			AtomicReference<Type[]> elementGenericTypeArgs
					= new AtomicReference<Type[]>(PodamConstants.NO_TYPES);
			realAttributeType = TypeManufacturerUtil.resolveGenericParameter(genericAttributeType,
                    typeArgsMap, elementGenericTypeArgs);
		} else {
			realAttributeType = attributeType;
		}

		Type[] genericTypeArgsAll = TypeManufacturerUtil.mergeActualAndSuppliedGenericTypes(
					attributeType, genericAttributeType, genericTypeArgs, typeArgsMap);

		AttributeMetadata attributeMetadata = new AttributeMetadata(
				attributeName, realAttributeType, genericAttributeType,
				genericTypeArgsAll, annotations, pojoClass, pojo);

		if (realAttributeType.isArray()) {

			// Array type

			attributeValue = resolveArrayElementValue(pojo, manufacturingCtx,
					attributeMetadata, typeArgsMap);

			// Collection
		} else if (Collection.class.isAssignableFrom(realAttributeType)) {

			attributeValue = resolveCollectionValueWhenCollectionIsPojoAttribute(
					pojo, manufacturingCtx, attributeMetadata, typeArgsMap);

            // Map
		} else if (Map.class.isAssignableFrom(realAttributeType)) {

			attributeValue = resolveMapValueWhenMapIsPojoAttribute(pojo,
					manufacturingCtx, attributeMetadata, typeArgsMap);

		}

		// For any other type, we use the PODAM strategy
		if (attributeValue == null) {

			Integer depth = manufacturingCtx.getPojos().get(realAttributeType);
			if (depth == null) {
				depth = -1;
			}
			if (depth <= strategy.getMaxDepth(pojoClass)) {

				manufacturingCtx.getPojos().put(realAttributeType, depth + 1);

				attributeValue = this.manufacturePojoInternal(
						realAttributeType, attributeMetadata, manufacturingCtx, genericTypeArgsAll);
				manufacturingCtx.getPojos().put(realAttributeType, depth);

			} else {

				attributeValue = resortToExternalFactory(manufacturingCtx,
						"Loop in {} production detected. Resorting to {} external factory",
						realAttributeType, genericTypeArgsAll);

			}
		}

		return attributeValue;
	}

	/**
	 * Delegates POJO manufacturing to an external factory
	 *
	 * @param <T>
	 *            The type of the instance to return
	 * @param manufacturingCtx
	 *            the manufacturing context
	 * @param msg
	 *            Message to log, must contain two parameters
	 * @param pojoClass
	 *            The class of which an instance is required
	 * @param genericTypeArgs
	 *            The generic type arguments for the current generic class
	 *            instance
	 * @return instance of POJO produced by external factory or null
	 */
	private <T> T resortToExternalFactory(ManufacturingContext manufacturingCtx,
			String msg, Class<T> pojoClass,
			Type... genericTypeArgs) {

		LOG.warn(msg, pojoClass, externalFactory.getClass().getName());
		if (manufacturingCtx.getConstructorOrdering() == Order.HEAVY_FIRST) {
			return externalFactory.manufacturePojoWithFullData(pojoClass, genericTypeArgs);
		} else {
			return externalFactory.manufacturePojo(pojoClass, genericTypeArgs);
		}
	}




	/**
	 * It returns a collection of some sort with some data in it.
	 *
	 *
	 * @param pojo
	 *            The POJO being analyzed
	 * @param manufacturingCtx
	 *            the manufacturing context
	 * @param attributeMetadata
	 *            The attribute's metadata
	 * @param typeArgsMap
	 *            a map relating the generic class arguments ("&lt;T, V&gt;" for
	 *            example) with their actual types
	 * @return a collection of some sort with some data in it
	 * @throws PodamMockeryException
	 *             An exception occurred while resolving the collection
	 * @throws IllegalArgumentException
	 *             If the field name is null or empty
	 */
	private Collection<? super Object> resolveCollectionValueWhenCollectionIsPojoAttribute(
			Object pojo, ManufacturingContext manufacturingCtx,
			AttributeMetadata attributeMetadata, Map<String, Type> typeArgsMap) {

		String attributeName = attributeMetadata.getAttributeName();

		// This needs to be generic because collections can be of any type
		Collection<Object> defaultValue = null;
		if (null != pojo && !Character.isDigit(attributeName.charAt(0))) {

			defaultValue = PodamUtils.getFieldValue(pojo, attributeName);
		}

		Collection<Object> retValue = null;
		if (null != defaultValue &&
				(defaultValue.getClass().getModifiers() & Modifier.PRIVATE) == 0) {
			/* Default collection, which is not immutable */
			retValue = defaultValue;
		} else {

			@SuppressWarnings("unchecked")
			Class<Collection<Object>> collectionType
					= (Class<Collection<Object>>) attributeMetadata.getAttributeType();
			retValue = strategy.getTypeValue(attributeMetadata, typeArgsMap, collectionType);
			if (null != retValue && null != defaultValue) {
				retValue.addAll(defaultValue);
			}
		}

		if (null == retValue) {
			return null;
		}

		try {

			Class<?> typeClass = null;

			AtomicReference<Type[]> elementGenericTypeArgs = new AtomicReference<Type[]>(
					PodamConstants.NO_TYPES);
			if (ArrayUtils.isEmpty(attributeMetadata.getAttrGenericArgs())) {

 				typeClass = findInheretedCollectionElementType(retValue,
						manufacturingCtx, elementGenericTypeArgs, typeArgsMap, attributeMetadata.getAttrGenericArgs());
			} else {
				Type actualTypeArgument = attributeMetadata.getAttrGenericArgs()[0];

				typeClass = TypeManufacturerUtil.resolveGenericParameter(actualTypeArgument,
                        typeArgsMap, elementGenericTypeArgs);
			}

			fillCollection(manufacturingCtx,
					attributeMetadata.getAttributeAnnotations(), attributeName,
					retValue, typeClass, elementGenericTypeArgs.get());

		} catch (SecurityException e) {
			throw new PodamMockeryException(RESOLVING_COLLECTION_EXCEPTION_STR,
					e);
		} catch (IllegalArgumentException e) {
			throw new PodamMockeryException(RESOLVING_COLLECTION_EXCEPTION_STR,
					e);
		} catch (InstantiationException e) {
			throw new PodamMockeryException(RESOLVING_COLLECTION_EXCEPTION_STR,
					e);
		} catch (IllegalAccessException e) {
			throw new PodamMockeryException(RESOLVING_COLLECTION_EXCEPTION_STR,
					e);
		} catch (ClassNotFoundException e) {
			throw new PodamMockeryException(RESOLVING_COLLECTION_EXCEPTION_STR,
					e);
		} catch (InvocationTargetException e) {
			throw new PodamMockeryException(RESOLVING_COLLECTION_EXCEPTION_STR,
					e);
		}

		return retValue;
	}

	/**
	 * Tries to find collection element type from collection object
	 *
	 * @param collection
	 *          The collection to be filled
	 * @param manufacturingCtx
	 *          the manufacturing context
	 * @param elementGenericTypeArgs
	 *          parameter to return generic arguments of collection element
	 * @param typeArgsMap
	 *          a map relating the generic class arguments ("&lt;T, V&gt;" for
	 *          example) with their actual types
	 * @param genericTypeArgs
	 *          The generic type arguments for the current generic class
	 *          instance
	 * @return
	 *        class type of collection element
	 */
	private Class<?> findInheretedCollectionElementType(
			Collection<Object> collection, ManufacturingContext manufacturingCtx,
			AtomicReference<Type[]> elementGenericTypeArgs,
			Map<String, Type> typeArgsMap, Type... genericTypeArgs) {

		Class<?> pojoClass = collection.getClass();
		Class<?> collectionClass = pojoClass;
		Type[] typeParams = collectionClass.getTypeParameters();
		main : while (typeParams.length < 1) {
			for (Type genericIface : collectionClass.getGenericInterfaces()) {
				Class<?> clazz = TypeManufacturerUtil.resolveGenericParameter(
						genericIface,typeArgsMap, elementGenericTypeArgs);
				if (Collection.class.isAssignableFrom(clazz)) {
					collectionClass = clazz;
					typeParams = elementGenericTypeArgs.get();
					continue main;
				}
			}
			Type type = collectionClass.getGenericSuperclass();
			if (type != null) {
				Class<?> clazz = TypeManufacturerUtil.resolveGenericParameter(
						type, typeArgsMap, elementGenericTypeArgs);
				if (Collection.class.isAssignableFrom(clazz)) {
					collectionClass = clazz;
					typeParams = elementGenericTypeArgs.get();
					continue main;
				}
			}
			if (Collection.class.equals(collectionClass)) {
				LOG.warn("Collection {} doesn't have generic types,"
						+ "will use Object instead", pojoClass);
				typeParams = new Type[] { Object.class };
			}
		}
		Class<?> elementTypeClass = TypeManufacturerUtil.resolveGenericParameter(typeParams[0],
					typeArgsMap, elementGenericTypeArgs);
		Type[] elementGenericArgs = ArrayUtils.addAll(
				elementGenericTypeArgs.get(), genericTypeArgs);
		elementGenericTypeArgs.set(elementGenericArgs);
		return elementTypeClass;
	}

	/**
	 * It fills a collection with the required number of elements of the
	 * required type.
	 *
	 * <p>
	 * This method has a so-called side effect. It updates the collection passed
	 * as argument.
	 * </p>
	 *
	 * @param manufacturingCtx
	 *            the manufacturing context
	 * @param annotations
	 *            The annotations for this attribute
	 * @param attributeName
	 *            The attribute name of collection in enclosing POJO class 
	 * @param collection
	 *            The Collection to be filled
	 * @param collectionElementType
	 *            The type of the collection element
	 * @param genericTypeArgs
	 *            The generic type arguments for the current generic class
	 *            instance
	 * @throws InstantiationException
	 *             If an exception occurred during instantiation
	 * @throws IllegalAccessException
	 *             If security was violated while creating the object
	 * @throws InvocationTargetException
	 *             If an exception occurred while invoking the constructor or
	 *             factory method
	 * @throws ClassNotFoundException
	 *             If it was not possible to create a class from a string
	 *
	 */
	private void fillCollection(ManufacturingContext manufacturingCtx,
			List<Annotation> annotations, String attributeName,
			Collection<? super Object> collection,
			Class<?> collectionElementType, Type... genericTypeArgs)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {

		// If the user defined a strategy to fill the collection elements,
		// we use it
		Holder<AttributeStrategy<?>> elementStrategyHolder
				= new Holder<AttributeStrategy<?>>();
		Holder<AttributeStrategy<?>> keyStrategyHolder = null;
		Integer nbrElements = TypeManufacturerUtil.findCollectionSize(strategy, annotations,
                collectionElementType, elementStrategyHolder, keyStrategyHolder);
		AttributeStrategy<?> elementStrategy = elementStrategyHolder.value;

		try {
			if (collection.size() > nbrElements) {

				collection.clear();
			}

			for (int i = collection.size(); i < nbrElements; i++) {

				// The default
				Object element;
				if (null != elementStrategy
						&& (elementStrategy instanceof ObjectStrategy)
								&& Object.class.equals(collectionElementType)) {
					LOG.debug("Element strategy is ObjectStrategy and collection element is of type Object: using the ObjectStrategy strategy");
					element = elementStrategy.getValue();
				} else if (null != elementStrategy
						&& !(elementStrategy instanceof ObjectStrategy)) {
					LOG.debug("Collection elements will be filled using the following strategy: "
							+ elementStrategy);
					element = TypeManufacturerUtil.returnAttributeDataStrategyValue(
                            collectionElementType, elementStrategy);
				} else {
					Map<String, Type> nullTypeArgsMap = new HashMap<String, Type>();
					element = manufactureAttributeValue(collection, manufacturingCtx,
							collectionElementType, collectionElementType,
							annotations, attributeName, nullTypeArgsMap, genericTypeArgs);
				}
				collection.add(element);
			}
		} catch (UnsupportedOperationException e) {

			LOG.warn("Cannot fill immutable collection {}", collection.getClass());
		}
	}

	/**
	 * It manufactures and returns a Map with at least one element in it
	 *
	 * @param pojo
	 *            The POJO being initialized
	 * @param manufacturingCtx
	 *            the manufacturing context
	 * @param attributeMetadata
	 *            The attribute's metadata
	 * @param typeArgsMap
	 *            a map relating the generic class arguments ("&lt;T, V&gt;" for
	 *            example) with their actual types
	 * @return Map with at least one element in it
	 *
	 * @throws IllegalArgumentException
	 *             <ul>
	 *             <li>If the attribute name is null or empty</li>
	 *             <li>If the array of types of the Map has length different
	 *             from 2</li>
	 *             </ul>
	 *
	 * @throws PodamMockeryException
	 *             If an error occurred while creating the Map object
	 */
	private Map<? super Object, ? super Object> resolveMapValueWhenMapIsPojoAttribute(
			Object pojo, ManufacturingContext manufacturingCtx,
			AttributeMetadata attributeMetadata, Map<String, Type> typeArgsMap) {

		String attributeName = attributeMetadata.getAttributeName();

		Map<Object, Object> defaultValue = null;
		if (null != pojo && !Character.isDigit(attributeName.charAt(0))) {

			defaultValue = PodamUtils.getFieldValue(pojo, attributeName);
		}

		Map<Object, Object> retValue;
		if (null != defaultValue &&
				(defaultValue.getClass().getModifiers() & Modifier.PRIVATE) == 0) {
			/* Default map, which is not immutable */
			retValue = defaultValue;
		} else {

			@SuppressWarnings("unchecked")
			Class<Map<Object,Object>> mapType
					= (Class<Map<Object, Object>>) attributeMetadata.getAttributeType();
			retValue = strategy.getTypeValue(attributeMetadata, typeArgsMap, mapType);
			if (null != retValue && null != defaultValue) {
				retValue.putAll(defaultValue);
			}
		}

		if (null == retValue) {
			return null;
		}

		try {

			Class<?> keyClass = null;

			Class<?> elementClass = null;

			AtomicReference<Type[]> keyGenericTypeArgs = new AtomicReference<Type[]>(
					PodamConstants.NO_TYPES);
			AtomicReference<Type[]> elementGenericTypeArgs = new AtomicReference<Type[]>(
					PodamConstants.NO_TYPES);
			if (ArrayUtils.isEmpty(attributeMetadata.getAttrGenericArgs())) {

				MapArguments mapArgs = findInheretedMapElementType(retValue,
						manufacturingCtx, typeArgsMap,
						attributeMetadata.getAttrGenericArgs());

				keyClass = mapArgs.getKeyClass();

				elementClass = mapArgs.getElementClass();

			} else {

				// Expected only key, value type
				if (attributeMetadata.getAttrGenericArgs().length != 2) {
					throw new IllegalStateException(
							"In a Map only key value generic type are expected,"
							+ "but received " + Arrays.toString(attributeMetadata.getAttrGenericArgs()));
				}

				Type[] actualTypeArguments = attributeMetadata.getAttrGenericArgs();
				keyClass = TypeManufacturerUtil.resolveGenericParameter(actualTypeArguments[0],
						typeArgsMap, keyGenericTypeArgs);
				elementClass = TypeManufacturerUtil.resolveGenericParameter(actualTypeArguments[1],
						typeArgsMap, elementGenericTypeArgs);
			}

			MapArguments mapArguments = new MapArguments();
			mapArguments.setAttributeName(attributeName);
			mapArguments.setAnnotations(attributeMetadata.getAttributeAnnotations());
			mapArguments.setMapToBeFilled(retValue);
			mapArguments.setKeyClass(keyClass);
			mapArguments.setElementClass(elementClass);
			mapArguments.setKeyGenericTypeArgs(keyGenericTypeArgs.get());
			mapArguments
					.setElementGenericTypeArgs(elementGenericTypeArgs.get());

			fillMap(mapArguments, manufacturingCtx);

		} catch (InstantiationException e) {
			throw new PodamMockeryException(MAP_CREATION_EXCEPTION_STR, e);
		} catch (IllegalAccessException e) {
			throw new PodamMockeryException(MAP_CREATION_EXCEPTION_STR, e);
		} catch (SecurityException e) {
			throw new PodamMockeryException(MAP_CREATION_EXCEPTION_STR, e);
		} catch (ClassNotFoundException e) {
			throw new PodamMockeryException(MAP_CREATION_EXCEPTION_STR, e);
		} catch (InvocationTargetException e) {
			throw new PodamMockeryException(MAP_CREATION_EXCEPTION_STR, e);
		}

		return retValue;
	}

	/**
	 * Finds key and element type arguments 
	 *
	 * @param map
	 *          The map being initialized
	 * @param manufacturingCtx
	 *          the manufacturing context
	 * @param typeArgsMap
	 *          a map relating the generic class arguments ("&lt;T, V&gt;" for
	 *          example) with their actual types
	 * @param genericTypeArgs
	 *          The generic type arguments for the current generic class
	 *          instance
	 * @return
	 *        Inherited map key and element types
	 *
	 */
	private MapArguments findInheretedMapElementType(Map<Object, Object> map,
			ManufacturingContext manufacturingCtx, Map<String, Type> typeArgsMap,
			Type... genericTypeArgs) {

		Class<?> pojoClass = map.getClass();
		Class<?> mapClass = pojoClass;
		AtomicReference<Type[]> elementGenericTypeArgs = new AtomicReference<Type[]>(
				PodamConstants.NO_TYPES);
		Type[] typeParams = mapClass.getTypeParameters();
		main : while (typeParams.length < 2) {
			for (Type genericIface : mapClass.getGenericInterfaces()) {
				Class<?> clazz = TypeManufacturerUtil.resolveGenericParameter(
						genericIface, typeArgsMap, elementGenericTypeArgs);
				if (Map.class.isAssignableFrom(clazz)) {
					typeParams = elementGenericTypeArgs.get();
					mapClass = clazz;
					continue main;
				}
			}
			Type type = mapClass.getGenericSuperclass();
			if (type != null) {
				Class<?> clazz = TypeManufacturerUtil.resolveGenericParameter(
						type, typeArgsMap, elementGenericTypeArgs);
				if (Map.class.isAssignableFrom(clazz)) {
					typeParams = elementGenericTypeArgs.get();
					mapClass = clazz;
					continue main;
				}
			}
			if (Map.class.equals(mapClass)) {
				LOG.warn("Map {} doesn't have generic types,"
						+ "will use Object, Object instead", pojoClass);
				typeParams = new Type[] { Object.class, Object.class };
			}
		}
		AtomicReference<Type[]> keyGenericTypeArgs = new AtomicReference<Type[]>(
				PodamConstants.NO_TYPES);
		Class<?> keyClass = TypeManufacturerUtil.resolveGenericParameter(typeParams[0],
					typeArgsMap, keyGenericTypeArgs);
		Class<?> elementClass = TypeManufacturerUtil.resolveGenericParameter(
				typeParams[1], typeArgsMap, elementGenericTypeArgs);

		Type[] keyGenericArgs = ArrayUtils.addAll(keyGenericTypeArgs.get(),
				genericTypeArgs);
		Type[] elementGenericArgs = ArrayUtils.addAll(elementGenericTypeArgs.get(),
				genericTypeArgs);

		MapArguments mapArguments = new MapArguments();
		mapArguments.setAnnotations(Arrays.<Annotation>asList(pojoClass.getAnnotations()));
		mapArguments.setMapToBeFilled(map);
		mapArguments.setKeyClass(keyClass);
		mapArguments.setElementClass(elementClass);
		mapArguments.setKeyGenericTypeArgs(keyGenericArgs);
		mapArguments.setElementGenericTypeArgs(elementGenericArgs);

		return mapArguments;
	}

	/**
	 * It fills a Map with the required number of elements of the required type.
	 *
	 * <p>
	 * This method has a so-called side-effect. It updates the Map given as
	 * argument.
	 * </p>
	 *
	 * @param mapArguments
	 *             The arguments POJO
	 * @param manufacturingCtx
	 *             Manufacturing context
	 * @throws InstantiationException
	 *             If an exception occurred during instantiation
	 * @throws IllegalAccessException
	 *             If security was violated while creating the object
	 * @throws InvocationTargetException
	 *             If an exception occurred while invoking the constructor or
	 *             factory method
	 * @throws ClassNotFoundException
	 *             If it was not possible to create a class from a string
	 *
	 */
	private void fillMap(MapArguments mapArguments, ManufacturingContext manufacturingCtx)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {

		// If the user defined a strategy to fill the collection elements,
		// we use it
		Holder<AttributeStrategy<?>> elementStrategyHolder
				= new Holder<AttributeStrategy<?>>();
		Holder<AttributeStrategy<?>> keyStrategyHolder
				= new Holder<AttributeStrategy<?>>();
		Integer nbrElements = TypeManufacturerUtil.findCollectionSize(strategy, mapArguments.getAnnotations(),
                mapArguments.getElementClass(), elementStrategyHolder,
                keyStrategyHolder);
		AttributeStrategy<?> keyStrategy = keyStrategyHolder.value;
		AttributeStrategy<?> elementStrategy = elementStrategyHolder.value;

		Map<? super Object, ? super Object> map = mapArguments.getMapToBeFilled();
		try {
			if (map.size() > nbrElements) {

				map.clear();
			}

			for (int i = map.size(); i < nbrElements; i++) {

				Object keyValue = null;

				Object elementValue = null;

				MapKeyOrElementsArguments valueArguments = new MapKeyOrElementsArguments();
				valueArguments.setAttributeName(mapArguments.getAttributeName());
				valueArguments.setMapToBeFilled(mapArguments.getMapToBeFilled());
				valueArguments.setAnnotations(mapArguments.getAnnotations());
				valueArguments.setKeyOrValueType(mapArguments.getKeyClass());
				valueArguments.setElementStrategy(keyStrategy);
				valueArguments.setGenericTypeArgs(mapArguments
						.getKeyGenericTypeArgs());

				keyValue = getMapKeyOrElementValue(valueArguments, manufacturingCtx);

				valueArguments.setKeyOrValueType(mapArguments.getElementClass());
				valueArguments.setElementStrategy(elementStrategy);
				valueArguments.setGenericTypeArgs(mapArguments
						.getElementGenericTypeArgs());

				elementValue = getMapKeyOrElementValue(valueArguments, manufacturingCtx);

				/* ConcurrentHashMap doesn't allow null values */
				if (elementValue != null || !(map instanceof ConcurrentHashMap)) {
					map.put(keyValue, elementValue);
				}
			}
		} catch (UnsupportedOperationException e) {

			LOG.warn("Cannot fill immutable map {}", map.getClass());
		}
	}

	/**
	 * It fills a Map key or value with the appropriate value, considering
	 * attribute-level customisation.
	 *
	 * @param keyOrElementsArguments
	 *            The arguments POJO
	 * @param manufacturingCtx
	 *             manufacturing context
	 * @return A Map key or value
	 * @throws InstantiationException
	 *             If an exception occurred during instantiation
	 * @throws IllegalAccessException
	 *             If security was violated while creating the object
	 * @throws InvocationTargetException
	 *             If an exception occurred while invoking the constructor or
	 *             factory method
	 * @throws IllegalArgumentException
	 *             <ul>
	 *             <li>If an illegal argument was passed</li>
	 *             <li>If an invalid value was set for a precise value in an
	 *             annotation and such value could not be converted to the
	 *             desired type</li>
	 *             </ul>
	 * @throws ClassNotFoundException
	 *             If manufactured class could not be loaded
	 */
	private Object getMapKeyOrElementValue(
			MapKeyOrElementsArguments keyOrElementsArguments,
			ManufacturingContext manufacturingCtx)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {

		Object retValue = null;

		if (null != keyOrElementsArguments.getElementStrategy()
				&& ObjectStrategy.class.isAssignableFrom(keyOrElementsArguments
						.getElementStrategy().getClass())
				&& Object.class.equals(keyOrElementsArguments
						.getKeyOrValueType())) {
			LOG.debug("Element strategy is ObjectStrategy and Map key or value type is of type Object: using the ObjectStrategy strategy");
			retValue = keyOrElementsArguments.getElementStrategy().getValue();
		} else if (null != keyOrElementsArguments.getElementStrategy()
				&& !ObjectStrategy.class
						.isAssignableFrom(keyOrElementsArguments
								.getElementStrategy().getClass())) {
			LOG.debug("Map key or value will be filled using the following strategy: "
					+ keyOrElementsArguments.getElementStrategy());
			retValue = TypeManufacturerUtil.returnAttributeDataStrategyValue(
                    keyOrElementsArguments.getKeyOrValueType(),
                    keyOrElementsArguments.getElementStrategy());

		} else {

			Map<String, Type> nullTypeArgsMap = new HashMap<String, Type>();
			retValue = manufactureAttributeValue(
					keyOrElementsArguments.getMapToBeFilled(),
					manufacturingCtx,
					keyOrElementsArguments.getKeyOrValueType(),
					keyOrElementsArguments.getKeyOrValueType(),
					keyOrElementsArguments.getAnnotations(),
					keyOrElementsArguments.getAttributeName(),
					nullTypeArgsMap,
					keyOrElementsArguments.getGenericTypeArgs());
		}
		return retValue;
	}

	/**
	 * It returns an Array with the first element set
	 *
	 *
	 * @param pojo
	 *            POJO containing attribute
	 * @param manufacturingCtx
	 *          the manufacturing context
	 * @param attributeMetadata
	 *            The attribute's metadata
	 * @param typeArgsMap
	 *            a map relating the generic class arguments ("&lt;T, V&gt;" for
	 *            example) with their actual types
	 * @return Array with the first element set
	 * @throws IllegalArgumentException
	 *             If an illegal argument was passed to the constructor
	 * @throws InstantiationException
	 *             If an exception occurred during instantiation
	 * @throws IllegalAccessException
	 *             If security was violated while creating the object
	 * @throws InvocationTargetException
	 *             If an exception occurred while invoking the constructor or
	 *             factory method
	 * @throws ClassNotFoundException
	 *             If it was not possible to create a class from a string
	 */
	private Object resolveArrayElementValue(Object pojo,
			ManufacturingContext manufacturingCtx,
			AttributeMetadata attributeMetadata,
			Map<String, Type> typeArgsMap) throws InstantiationException,
			IllegalAccessException, InvocationTargetException,
			ClassNotFoundException {

		@SuppressWarnings("unchecked")
		Class<Object> arrayType
				= (Class<Object>) attributeMetadata.getAttributeType();
		Object array = strategy.getTypeValue(attributeMetadata, typeArgsMap, arrayType);

		Class<?> componentType = array.getClass().getComponentType();
		Type genericComponentType;
		AtomicReference<Type[]> genericTypeArgs = new AtomicReference<Type[]>(
				PodamConstants.NO_TYPES);
		Type genericType = attributeMetadata.getAttributeGenericType();
		if (genericType instanceof GenericArrayType) {
			genericComponentType = ((GenericArrayType) genericType).getGenericComponentType();
			if (genericComponentType instanceof TypeVariable) {
				TypeVariable<?> componentTypeVariable
						 = (TypeVariable<?>) genericComponentType;
				final Type resolvedType
						 = typeArgsMap.get(componentTypeVariable.getName());
				componentType
						 = TypeManufacturerUtil.resolveGenericParameter(resolvedType, typeArgsMap,
								genericTypeArgs);
			}
		} else {
			genericComponentType = componentType;
		}

		// If the user defined a strategy to fill the collection elements,
		// we use it
		Holder<AttributeStrategy<?>> elementStrategyHolder
				= new Holder<AttributeStrategy<?>>();
		Holder<AttributeStrategy<?>> keyStrategyHolder = null;
		Integer nbrElements = TypeManufacturerUtil.findCollectionSize(strategy,
				attributeMetadata.getAttributeAnnotations(),
				attributeMetadata.getAttributeType(),
				elementStrategyHolder, keyStrategyHolder);
		AttributeStrategy<?> elementStrategy = elementStrategyHolder.value;

		Object arrayElement = null;
		for (int i = 0; i < nbrElements; i++) {

			// The default
			if (null != elementStrategy
					&& (elementStrategy instanceof ObjectStrategy)
					&& Object.class.equals(componentType)) {
				LOG.debug("Element strategy is ObjectStrategy and array element is of type Object: using the ObjectStrategy strategy");
				arrayElement = elementStrategy.getValue();
			} else if (null != elementStrategy
					&& !(elementStrategy instanceof ObjectStrategy)) {
				LOG.debug("Array elements will be filled using the following strategy: "
						+ elementStrategy);
				arrayElement = TypeManufacturerUtil.returnAttributeDataStrategyValue(componentType,
						elementStrategy);

			} else {

				arrayElement = manufactureAttributeValue(array, manufacturingCtx,
						componentType, genericComponentType,
						attributeMetadata.getAttributeAnnotations(),
						attributeMetadata.getAttributeName(),
						typeArgsMap, genericTypeArgs.get());

			}

			Array.set(array, i, arrayElement);

		}

		return array;
	}


	/**
	 * Given a constructor it manufactures and returns the parameter values
	 * required to invoke it
	 *
	 * @param constructor
	 *            The constructor for which parameter values are required
	 * @param pojoClass
	 *            The POJO class containing the constructor
	 * @param manufacturingCtx
	 *          the manufacturing context
	 * @param typeArgsMap
	 *            a map relating the generic class arguments ("&lt;T, V&gt;" for
	 *            example) with their actual types
	 * @param genericTypeArgs
	 *            The generic type arguments for the current generic class
	 *            instance
	 *
	 * @return The parameter values required to invoke the constructor
	 * @throws IllegalArgumentException
	 *             If an illegal argument was passed to the constructor
	 * @throws InstantiationException
	 *             If an exception occurred during instantiation
	 * @throws IllegalAccessException
	 *             If security was violated while creating the object
	 * @throws InvocationTargetException
	 *             If an exception occurred while invoking the constructor or
	 *             factory method
	 * @throws ClassNotFoundException
	 *             If it was not possible to create a class from a string
	 */
	private Object[] getParameterValuesForConstructor(
			Constructor<?> constructor, Class<?> pojoClass,
			ManufacturingContext manufacturingCtx, Map<String, Type> typeArgsMap,
			Type... genericTypeArgs)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {

		Class<?>[] parameterTypes = constructor.getParameterTypes();

		if (parameterTypes.length == 0) {

			return PodamConstants.NO_ARGS;

		} else {

			Object[] parameterValues = new Object[parameterTypes.length];

			Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
			Type[] genericTypes = constructor.getGenericParameterTypes();
			String ctorName = Arrays.toString(genericTypes);

			for (int idx = 0; idx < parameterTypes.length; idx++) {

				List<Annotation> annotations = Arrays
						.asList(parameterAnnotations[idx]);

				Type genericType = (idx < genericTypes.length) ?
						genericTypes[idx] : parameterTypes[idx];

				parameterValues[idx] = manufactureParameterValue(pojoClass,
						idx + ctorName, parameterTypes[idx], genericType,
						annotations, typeArgsMap, manufacturingCtx, genericTypeArgs);
			}
			return parameterValues;
		}
	}

	/**
	 * Given a method it manufactures and returns the parameter values
	 * required to invoke it
	 *
	 * @param method
	 *            The method for which parameter values are required
	 * @param pojoClass
	 *            The POJO class containing the constructor
	 * @param manufacturingCtx
	 *          the manufacturing context
	 * @param typeArgsMap
	 *            a map relating the generic class arguments ("&lt;T, V&gt;" for
	 *            example) with their actual types
	 * @param genericTypeArgs
	 *            The generic type arguments for the current generic class
	 *            instance
	 *
	 * @return The parameter values required to invoke the method
	 * @throws IllegalArgumentException
	 *             If an illegal argument was passed to the method
	 * @throws InstantiationException
	 *             If an exception occurred during instantiation
	 * @throws IllegalAccessException
	 *             If security was violated while creating the object
	 * @throws InvocationTargetException
	 *             If an exception occurred while invoking the constructor or
	 *             factory method
	 * @throws ClassNotFoundException
	 *             If it was not possible to create a class from a string
	 */
	private Object[] getParameterValuesForMethod(
			Method method, Class<?> pojoClass,
			ManufacturingContext manufacturingCtx, Map<String, Type> typeArgsMap,
			Type... genericTypeArgs)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {

		Class<?>[] parameterTypes = method.getParameterTypes();

		if (parameterTypes.length == 0) {

			return PodamConstants.NO_ARGS;

		} else {

			Object[] parameterValues = new Object[parameterTypes.length];

			Annotation[][] parameterAnnotations = method.getParameterAnnotations();
			Type[] genericTypes = method.getGenericParameterTypes();
			String methodName = Arrays.toString(genericTypes);

			for (int idx = 0; idx < parameterTypes.length; idx++) {

				List<Annotation> annotations = Arrays
						.asList(parameterAnnotations[idx]);

				Type genericType = (idx < genericTypes.length) ?
						genericTypes[idx] : parameterTypes[idx];

				parameterValues[idx] = manufactureParameterValue(pojoClass,
						idx + methodName, parameterTypes[idx], genericType,
						annotations, typeArgsMap, manufacturingCtx, genericTypeArgs);
			}
			return parameterValues;
		}
	}

	/**
	 * Manufactures and returns the parameter value for method required to
	 * invoke it
	 *
	 * @param pojoClass pojo class
	 * @param parameterName name of parameter
	 * @param parameterType type of parameter
	 * @param genericType generic type of parameter
	 * @param annotations parameter annotations
	 * @param typeArgsMap map for resolving generic types
	 * @param manufacturingCtx
	 *            the manufacturing context
	 * @param genericTypeArgs
	 *            The generic type arguments for the current generic class
	 *            instance
	 *
	 * @return The parameter values required to invoke the constructor
	 * @throws IllegalArgumentException
	 *             If an illegal argument was passed to the constructor
	 * @throws InstantiationException
	 *             If an exception occurred during instantiation
	 * @throws IllegalAccessException
	 *             If security was violated while creating the object
	 * @throws InvocationTargetException
	 *             If an exception occurred while invoking the constructor or
	 *             factory method
	 * @throws ClassNotFoundException
	 *             If it was not possible to create a class from a string
	 */
	private Object manufactureParameterValue(Class<?> pojoClass,
			String parameterName, Class<?> parameterType, Type genericType,
			final List<Annotation> annotations, final Map<String, Type> typeArgsMap,
			ManufacturingContext manufacturingCtx,
			Type... genericTypeArgs)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {

		AttributeStrategy<?> attributeStrategy
				= TypeManufacturerUtil.findAttributeStrategy(strategy, annotations, parameterType);
		if (null != attributeStrategy) {

			LOG.debug("The parameter: " + genericType
                    + " will be filled using the following strategy: "
                    + attributeStrategy);

			return TypeManufacturerUtil.returnAttributeDataStrategyValue(parameterType,
                    attributeStrategy);
		}

		Map<String, Type> typeArgsMapForParam;
		if (genericType instanceof ParameterizedType) {
			typeArgsMapForParam = new HashMap<String, Type>(typeArgsMap);
			ParameterizedType parametrizedType =
					(ParameterizedType) genericType;

			TypeVariable<?>[] argumentTypes = parameterType.getTypeParameters();
			Type[] argumentGenericTypes = parametrizedType.getActualTypeArguments();

			for (int k = 0; k < argumentTypes.length; k++) {
				if (argumentGenericTypes[k] instanceof Class) {
					Class<?> genericParam = (Class<?>) argumentGenericTypes[k];
					typeArgsMapForParam.put(argumentTypes[k].getName(), genericParam);
				}
			}
		} else {
			typeArgsMapForParam = typeArgsMap;
		}

		return manufactureAttributeValue(pojoClass, manufacturingCtx, parameterType,
				genericType, annotations, parameterName, typeArgsMapForParam,
				genericTypeArgs);
	}

    /**
     * Returns a value for an abstract type or interface if possible.
     * @param pojoClass The Pojo class
     * @param pojoMetadata The Pojo metadata
     * @param manufacturingCtx The manufacturing context
     * @param genericTypeArgs The generic type arguments map
     * @param typeArgsMap 
     *            a map relating the generic class arguments ("&lt;T, V&gt;" for
     *            example) with their actual types
     * @param <T> The type of the value to be returned
     * @return a value or null, if manufacturing didn't succeed
     * @throws InstantiationException If a problem occurred while instantiating the object
     * @throws IllegalAccessException If a problem occurred while instantiating the object
     * @throws InvocationTargetException If a problem occurred while instantiating the object
     * @throws ClassNotFoundException If a problem occurred while instantiating the object
     */
    private <T> T getValueForAbstractType(Class<T> pojoClass,
                                          AttributeMetadata pojoMetadata,
                                          ManufacturingContext manufacturingCtx,
                                          Map<String, Type> typeArgsMap,
                                          Type[] genericTypeArgs)
            throws InstantiationException, IllegalAccessException,
            InvocationTargetException, ClassNotFoundException {

        Class<? extends T> specificClass = strategy.getSpecificClass(pojoClass);

        if (!specificClass.equals(pojoClass)) {

            return this.manufacturePojoInternal(specificClass, pojoMetadata,
                    manufacturingCtx, genericTypeArgs);
        }

        Class<?> factory = strategy.getFactoryClass(pojoClass);
        if (factory != null) {
            T retValue = instantiatePojoWithFactory(factory, pojoClass,
                manufacturingCtx, typeArgsMap, genericTypeArgs);
            if (retValue != null) {
                return retValue;
            }
        }

        return resortToExternalFactory(manufacturingCtx,
                "Cannot instantiate a class {}. Resorting to {} external factory",
                pojoClass, genericTypeArgs);
    }


}
