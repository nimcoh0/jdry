package org.softauto.logger;

import io.netty.handler.logging.LogLevel;
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
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;


//@Aspect
public class Tracer {

	private static final Marker TESTER = MarkerManager.getMarker("TESTER");



	//@Before("execution(* *(..))  && !within(org.softauto..*)")
	//@Before(value = "tracePointcut()")
	public synchronized void trace(JoinPoint joinPoint) throws Throwable {
		if (joinPoint.getSignature() instanceof MethodSignature) {
			handleMethod(joinPoint);
		}
		if (joinPoint.getSignature() instanceof ConstructorSignature) {
			handleConstructor(joinPoint);
		}
		//return joinPoint.proceed();
	}

	//@AfterReturning(pointcut = "execution(* *(..)) && !within(org.softauto..*)", returning = "result")
	public synchronized   void returning(JoinPoint joinPoint,Object result) throws Throwable{
		if (joinPoint.getSignature() instanceof MethodSignature) {
			handleExitMethod(joinPoint,result);
		}
		if (joinPoint.getSignature() instanceof ConstructorSignature) {
			handleExitConstructor(joinPoint,result);
		}
	}


	public  synchronized void handleConstructor(JoinPoint joinPoint) throws Throwable {
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
		ConstructorSignature constructorSignature = (ConstructorSignature) joinPoint.getSignature();
		org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(constructorSignature.getDeclaringType());
		//Logger logger = LoggerFactory.getLogger(constructorSignature.getDeclaringType());
		String clazz = constructorSignature.getDeclaringType().getTypeName();
		//Log annotation = (Log) constructorSignature.getConstructor().getAnnotation(Log.class);
		//LogLevel level = LogLevel.DEBUG;
		Level level = Level.DEBUG;
		ChronoUnit unit = ChronoUnit.MILLIS;
		boolean showArgs = true;
		boolean showResult = true;
		boolean showExecutionTime = true;
		Object[] args = joinPoint.getArgs();
		Class[] parameterTypes = codeSignature.getParameterTypes();
		String[] params = codeSignature.getParameterNames();
		logger.log(level,TESTER, entry(constructorSignature.getConstructor().getName(), showArgs, params, args, parameterTypes));
		//log(logger, level,
			//	entry(constructorSignature.getConstructor().getName(), showArgs, params, args, parameterTypes));
		Temporal start = Instant.now();

		Temporal end = Instant.now();
		String duration = String.format("%s %s", unit.between(start, end), unit.name().toLowerCase());
		//log(logger, level, exit(constructorSignature.getConstructor().getName(), duration, null, showResult,
				//showExecutionTime));

	}

	public synchronized void handleMethod(JoinPoint joinPoint) throws Throwable {
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(method.getDeclaringClass());
		//Logger logger = LoggerFactory.getLogger(method.getDeclaringClass());
		String clazz = method.getDeclaringClass().getTypeName();
		//Log annotation = method.getAnnotation(Log.class);
		//LogLevel level = LogLevel.DEBUG;
		Level level = Level.DEBUG;
		ChronoUnit unit = ChronoUnit.MILLIS;
		boolean showArgs = true;
		boolean showResult = true;
		boolean showExecutionTime = true;
		//Marker marker = new MarkerManager();
		String methodName = method.getName();
		Object[] methodArgs = joinPoint.getArgs();
		Class[] parameterTypes = codeSignature.getParameterTypes();
		String[] methodParams = codeSignature.getParameterNames();
		//log(logger, level, entry(methodName, showArgs, methodParams, methodArgs, parameterTypes));
		logger.log(level,TESTER,entry(methodName, showArgs, methodParams, methodArgs, parameterTypes));
		Temporal start = Instant.now();

		Temporal end = Instant.now();
		String duration = String.format("%s %s", unit.between(start, end), unit.name().toLowerCase());
		//log(logger, level, exit(methodName, duration, null, showResult, showExecutionTime));
		//logger.log(level, exit(methodName, showArgs, methodParams, methodArgs, parameterTypes));
	}

	public  synchronized void handleExitConstructor(JoinPoint joinPoint,Object result) throws Throwable {
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
		ConstructorSignature constructorSignature = (ConstructorSignature) joinPoint.getSignature();
		org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(constructorSignature.getDeclaringType());
		//Logger logger = LoggerFactory.getLogger(constructorSignature.getDeclaringType());
		String clazz = constructorSignature.getDeclaringType().getTypeName();
		//Log annotation = (Log) constructorSignature.getConstructor().getAnnotation(Log.class);
		//LogLevel level = LogLevel.DEBUG;
		Level level = Level.DEBUG;
		ChronoUnit unit = ChronoUnit.MILLIS;
		boolean showArgs = true;
		boolean showResult = true;
		boolean showExecutionTime = true;
		Object[] args = joinPoint.getArgs();
		Class[] parameterTypes = codeSignature.getParameterTypes();
		String[] params = codeSignature.getParameterNames();
		//logger.log(level,TESTER, entry(constructorSignature.getConstructor().getName(), showArgs, params, args, parameterTypes));
		//log(logger, level,
		//	entry(constructorSignature.getConstructor().getName(), showArgs, params, args, parameterTypes));
		Temporal start = Instant.now();

		Temporal end = Instant.now();
		String duration = String.format("%s %s", unit.between(start, end), unit.name().toLowerCase());
		logger.log(level,TESTER, exit(constructorSignature.getConstructor().getName(), duration, result, showResult,
			showExecutionTime));

	}

	public synchronized void handleExitMethod(JoinPoint joinPoint,Object result) throws Throwable {
		CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(method.getDeclaringClass());
		//Logger logger = LoggerFactory.getLogger(method.getDeclaringClass());
		String clazz = method.getDeclaringClass().getTypeName();
		//Log annotation = method.getAnnotation(Log.class);
		//LogLevel level = LogLevel.DEBUG;
		Level level = Level.DEBUG;
		ChronoUnit unit = ChronoUnit.MILLIS;
		boolean showArgs = true;
		boolean showResult = true;
		boolean showExecutionTime = true;
		//Marker marker = new MarkerManager();
		String methodName = method.getName();
		Object[] methodArgs = joinPoint.getArgs();
		Class[] parameterTypes = codeSignature.getParameterTypes();
		String[] methodParams = codeSignature.getParameterNames();
		//log(logger, level, entry(methodName, showArgs, methodParams, methodArgs, parameterTypes));
		//logger.log(level,TESTER,entry(methodName, showArgs, methodParams, methodArgs, parameterTypes));
		Temporal start = Instant.now();

		Temporal end = Instant.now();
		String duration = String.format("%s %s", unit.between(start, end), unit.name().toLowerCase());
		logger.log(level,TESTER, exit(methodName, duration, result, showResult, showExecutionTime));
		//logger.log(level, exit(methodName, showArgs, methodParams, methodArgs, parameterTypes));
	}


	static String entry(String methodName, boolean showArgs, String[] params, Object[] args, Class[] parameterTypes) {
		StringJoiner message = new StringJoiner(" ").add("Started").add(methodName).add("method");
		if (showArgs && Objects.nonNull(params) && Objects.nonNull(args) && params.length == args.length) {
			Map<String, Object> values = new HashMap<>(params.length);
			for (int i = 0; i < params.length; i++) {
				values.put(params[i], new Serializer().serialize(args[i]));
			}
			message.add("with args:").add(values.toString());
		}
		return message.toString();
	}

	static String exit(String methodName, String duration, Object result, boolean showResult,
			boolean showExecutionTime) {
		StringJoiner message = new StringJoiner(" ").add("Finished").add(methodName).add("method");
		if (showExecutionTime) {
			message.add("in").add(duration);
		}
		if (showResult) {
			if (result != null) {
				message.add("with return:").add(new Serializer().serialize(result).toString());
			} else {
				message.add("no return");
			}
		}
		return message.toString();
	}

	static void log(Logger logger, LogLevel level, String message) {
		switch (level) {
		case DEBUG:
			logger.debug(message);
			break;
		case TRACE:
			logger.trace(message);
			break;
		case WARN:
			logger.warn(message);
			break;
		case ERROR:
			logger.error(message);
			break;
		default:
			logger.info(message);
		}
	}

}
