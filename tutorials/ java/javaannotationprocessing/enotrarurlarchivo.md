

[JAVA ANNOTATION PROCESSORS – CREATING CONFIGURATIONS](https://cloudogu.com/en/blog/Java-Annotation-Processors_2-Creating-Configurations)
```java
Filer filer = processingEnv.getFiler();
FileObject fileObject = filer.getResource(StandardLocation.CLASS_OUTPUT, "", "extensions.xml");
File extensionsFile = new File(fileObject.toUri());

```