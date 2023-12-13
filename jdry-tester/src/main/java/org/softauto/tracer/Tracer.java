package org.softauto.tracer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.ConstructorSignature;
import org.aspectj.lang.reflect.MethodSignature;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * aspectj class impl tracker
 */
@Aspect
public class Tracer {

	private static final Marker TESTER = MarkerManager.getMarker("TESTER");

	@Before("execution(* *(..))  && !within(org.softauto..*)")
	public synchronized void trace(JoinPoint joinPoint) throws Throwable {
		if (joinPoint.getSignature() instanceof MethodSignature) {
			handleMethod(joinPoint);
		}
		if (joinPoint.getSignature() instanceof ConstructorSignature) {
			handleConstructor(joinPoint);
		}

	}

	@AfterReturning(pointcut = "execution(* *(..)) && !within(org.softauto..*)", returning = "result")
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
		Level level = Level.DEBUG;
		boolean showArgs = true;
		Object[] args = joinPoint.getArgs();
		Class[] parameterTypes = codeSignature.getParameterTypes();
		String[] params = codeSignature.getParameterNames();
		logger.log(level,TESTER, entry(constructorSignature.getConstructor().getName(), showArgs, params, args, parameterTypes));
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
		logger.log(level,TESTER,entry(methodName, showArgs, methodParams, methodArgs, parameterTypes));
	}

	public  synchronized void handleExitConstructor(JoinPoint joinPoint,Object result) throws Throwable {
		ConstructorSignature constructorSignature = (ConstructorSignature) joinPoint.getSignature();
		org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(constructorSignature.getDeclaringType());
		Level level = Level.DEBUG;
		ChronoUnit unit = ChronoUnit.MILLIS;
		boolean showResult = true;
		boolean showExecutionTime = true;
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
		Level level = Level.DEBUG;
		ChronoUnit unit = ChronoUnit.MILLIS;
		boolean showResult = true;
		boolean showExecutionTime = true;
		String methodName = method.getName();
		Temporal start = Instant.now();
		Temporal end = Instant.now();
		String duration = String.format("%s %s", unit.between(start, end), unit.name().toLowerCase());
		logger.log(level,TESTER, exit(methodName, duration, result, showResult, showExecutionTime));
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


}
