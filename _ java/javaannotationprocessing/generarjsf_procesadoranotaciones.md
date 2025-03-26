# Generar anotaciones con el procesador

[How to generate xhtml files in webapp using javac annotation processor](https://stackoverflow.com/questions/52525655/how-to-generate-xhtml-files-in-webapp-using-javac-annotation-processor)


```java



 FileObject f = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "DUMMY");
  Path p = Paths.get(f.toUri())
          .getParent()  // {PROJECT_ROOT}/target/generated-sources/annotations
          .getParent()  // {PROJECT_ROOT}/target/generated-sources
          .getParent()  // {PROJECT_ROOT}/target
          .getParent(); // {PROJECT_ROOT}
  FileWriter fw = new FileWriter(new File(p.toFile(), "src/main/webapp/generated.xhtml"));
  fw.append("some content...");
  fw.close();)

```
