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

public class MyContainer {

	private MyContainer() {
	}

	private static final Map<String, Object> mapper = new HashMap<>();

	public static Object getMappingClass(String path) {
		return mapper.get(path);
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
					String path = ((MyMapping)annotation).value();
					mapper.put(path, obj);
				}
			}
		}
	}
}
