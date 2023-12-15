package org.softauto.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;


@Aspect
public abstract class Tracer {

	private static final Marker TRACER = MarkerManager.getMarker("TRACER");

	static String domain = null;

	@Pointcut
	public void tracePointcut(){

	};

	@Before(value = "tracePointcut() ")
	public synchronized void trace(JoinPoint joinPoint) throws Throwable {
			if (joinPoint.getSignature() instanceof MethodSignature) {
				if (domain == null) {
					domain = joinPoint.getSignature().getDeclaringType().getPackage().getName();
				}
				handleMethod(joinPoint);
			}
			if (joinPoint.getSignature() instanceof ConstructorSignature) {
				if (domain == null) {
					domain = joinPoint.getSignature().getDeclaringType().getPackage().getName();
				}
				handleConstructor(joinPoint);
			}

	}



	@AfterReturning(value = "tracePointcut() ", returning = "result")
	public synchronized   void returning(JoinPoint joinPoint,Object result) throws Throwable{
			if (joinPoint.getSignature() instanceof MethodSignature) {
				handleExitMethod(joinPoint, result);
			}
			if (joinPoint.getSignature() instanceof ConstructorSignature) {
				handleExitConstructor(joinPoint, result);
			}

	}






	public  synchronized void handleConstructor(JoinPoint joinPoint) throws Throwable {
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
		ConstructorSignature constructorSignature = (ConstructorSignature) joinPoint.getSignature();
		org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(constructorSignature.getDeclaringType());
		Level level = Level.DEBUG;
		boolean showArgs = true;
		Object[] args = joinPoint.getArgs();
		Class[] parameterTypes = codeSignature.getParameterTypes();
		String[] params = codeSignature.getParameterNames();
		logger.log(level,TRACER, entry(constructorSignature.getConstructor().getName(), showArgs, params, args, parameterTypes));
	}

	public synchronized void handleMethod(JoinPoint joinPoint) throws Throwable {
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(method.getDeclaringClass());
		Level level = Level.DEBUG;
		boolean showArgs = true;
		String methodName = method.getName();
		Object[] methodArgs = joinPoint.getArgs();
		Class[] parameterTypes = codeSignature.getParameterTypes();
		String[] methodParams = codeSignature.getParameterNames();
		logger.log(level,TRACER,entry(methodName, showArgs, methodParams, methodArgs, parameterTypes));
	}

	public  synchronized void handleExitConstructor(JoinPoint joinPoint,Object result) throws Throwable {
		ConstructorSignature constructorSignature = (ConstructorSignature) joinPoint.getSignature();
		org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(constructorSignature.getDeclaringType());
		Level level = Level.DEBUG;
		boolean showResult = true;
		boolean showExecutionTime = true;
		logger.log(level,TRACER, exit(constructorSignature.getConstructor().getName(), result, showResult,
			showExecutionTime));

	}

	public synchronized void handleExitMethod(JoinPoint joinPoint,Object result) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(method.getDeclaringClass());
		Level level = Level.DEBUG;
		boolean showResult = true;
		boolean showExecutionTime = true;
		String methodName = method.getName();
		logger.log(level,TRACER, exit(methodName, result, showResult, showExecutionTime));
	}


	static String entry(String methodName, boolean showArgs, String[] params, Object[] args, Class[] parameterTypes) {
		StringJoiner message = new StringJoiner(" ").add("Started").add(methodName).add("method");
		if (showArgs && Objects.nonNull(params) && Objects.nonNull(args) && params.length == args.length) {
			Map<String, Object> values = new HashMap<>(params.length);
			for (int i = 0; i < params.length; i++) {
				if(args[i].getClass().getTypeName().contains(domain))
					values.put(params[i], new Serializer().serialize(args[i]));
			}
			message.add("with args:").add(values.toString());
		}
		return message.toString();
	}

	static String exit(String methodName,  Object result, boolean showResult,
			boolean showExecutionTime) {
		StringJoiner message = new StringJoiner(" ").add("Finished").add(methodName).add("method");

		if (showResult) {
			if (result != null) {
				if(result.getClass().getTypeName().contains(domain))
					message.add("with return:").add(new Serializer().serialize(result).toString());
			} else {
				message.add("no return");
			}
		}
		return message.toString();
	}



}
