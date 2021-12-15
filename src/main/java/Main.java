import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class Main {
    private static List<String> lines;
    public static void main(String[] args) {
        String[] targetDirs = args[0].split(",");
        String workDir = args[1];
        lines = new ArrayList<>();
        lines.add("FileName;FileType;Content");
        for (String targetDir:targetDirs) {
            try (Stream<Path> paths = Files.walk(Paths.get(targetDir))) {
                paths
                        .forEach(path -> {
                            if (!Files.isSymbolicLink(path)) {
                                String fileName = path.getFileName().toString().toLowerCase();
                                if (fileName.contains("log4j")) {
                                    String fileType = Files.isDirectory(path) ? "directory" : "regularFile";
                                    String line = path + ";" + fileType + ";";
                                    lines.add(line);
                                }

                                if (Files.isRegularFile(path) && (fileName.endsWith(".jar") || fileName.endsWith(".war"))) {
                                    handlerJar(path);
                                }
                            }

                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        save(workDir);

    }

    private static void handlerJar(Path path) {
        try {
            if (path.toFile().length() == 0) {
                return;
            }
            JarFile file = new JarFile(path.toString());

            for (Enumeration<JarEntry> vals = file.entries(); vals.hasMoreElements();) {
                JarEntry entry = vals.nextElement();
                String fileName = entry.getName();
                if (fileName.toLowerCase().contains("log4j")) {
                    String line = path + ";innerClass;" + fileName;
                    lines.add(line);
                    return;
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR with " + path);
            e.printStackTrace();
        }

    }

    private static void save(String workDir) {
        try {
            String fileName = workDir + "/" + "result_" + System.currentTimeMillis() + ".csv";
            Path file = Paths.get(fileName);
            Files.write(file, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
