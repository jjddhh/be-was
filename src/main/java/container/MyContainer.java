package container;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import container.annotation.MyMapping;
import webserver.http.Method;

public class MyContainer {

	private MyContainer() {
	}

	private static final Map<Mapping, Object> mapper = new HashMap<>();

	public static Object getMappingClass(Mapping mapping) {
		return mapper.get(mapping);
	}

	public static void start(Class<?> componentScanRoot) throws ReflectiveOperationException {
		Package rootPackage = componentScanRoot.getPackage();

		Reflections reflections = new Reflections(new ConfigurationBuilder()
			.setUrls(ClasspathHelper.forPackage(rootPackage.getName()))
			.setScanners(new SubTypesScanner(false), new ResourcesScanner()));

		Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

		for (Class<?> clazz : classes) {
			Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();

			for (Annotation annotation : declaredAnnotations) {
				if (annotation instanceof MyMapping) {
					Constructor<?> constructor = clazz.getConstructor();
					Object obj = constructor.newInstance();
					Mapping mapping = createMapping((MyMapping) annotation);
					mapper.put(mapping, obj);
				}
			}
		}
	}

	public static Mapping createMapping(MyMapping annotation) {
		String url = annotation.url();
		Method method = annotation.method();
		return new Mapping(url, method);
	}
}
