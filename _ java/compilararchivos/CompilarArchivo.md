# Compilar Archivos

```java
       showMessage("Paso A: " + fileManager.getPackageName() + ".model." + fileManager.getClassName());

       String filePathClass = path.replace(File.separatorChar, '.');
        filePathClass = filePathClass.replace(".java", ".class");
       String filePathWithoutExtension = path.replace(".java", "");
        
         JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
       int result = compiler.run(null, null, null, "path/to/your/JavaFile.java");
showMessage("Voy a compilar "+path);
        int result = compiler.run(null, null, null, path);

        if (result == 0) {
           showMessage("Compilation successful");
        } else {
 showMessage("Compilation failed");
        }

``