package refactoring.kata.scanner;

import org.objectweb.asm.ClassReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author meixuesong
 */
public class JavaScanner {
    private static Logger logger = LoggerFactory.getLogger(JavaScanner.class);

    private List<String> ignorePackages = new ArrayList<>();

    public JavaScanner() {
    }

    public JavaScanner(List<String> ignorePackages) {
        this.ignorePackages = ignorePackages;
    }

    public CallGraph scan(String fullClassName) {
        CallGraphClassVisitor callGraphClassVisitor = new CallGraphClassVisitor(ignorePackages);
        try {
            String path = new File(".").getCanonicalPath();
            String classFile = path + "/target/classes/" + fullClassName.replace('.', '/') + ".class";
            Path classFilePath = Paths.get(classFile);

            ClassReader reader;
            if (Files.exists(classFilePath)) {
                reader = new ClassReader(new BufferedInputStream(new FileInputStream(classFilePath.toFile())));
            } else {
                reader = new ClassReader(fullClassName);
            }
            reader.accept(callGraphClassVisitor, 0);

            return callGraphClassVisitor.getCallGraph();
        } catch (IOException e) {
            logger.error("failed to scan {}", fullClassName, e);
            throw new RuntimeException("Failed to scan " + fullClassName, e);
        }
    }

    public CallGraph scanJar(String jarFileName) {
        CallGraphClassVisitor callGraphClassVisitor = new CallGraphClassVisitor(ignorePackages);
        scanSingleJar(jarFileName, callGraphClassVisitor);
        return callGraphClassVisitor.getCallGraph();
    }

    public CallGraph scanJars(List<String> jarFiles) {
        CallGraphClassVisitor callGraphClassVisitor = new CallGraphClassVisitor(ignorePackages);
        for (String jarFile : jarFiles) {
            scanSingleJar(jarFile, callGraphClassVisitor);
        }

        return callGraphClassVisitor.getCallGraph();
    }

    private void scanSingleJar(String jarFileName, CallGraphClassVisitor callGraphClassVisitor) {
        try (JarFile jarFile = new JarFile(jarFileName)) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                if (!entry.getName().endsWith(".class")) {
                    continue;
                }

                InputStream stream = new BufferedInputStream(jarFile.getInputStream(entry), 1024);
                ClassReader reader = new ClassReader(stream);
                reader.accept(callGraphClassVisitor, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
