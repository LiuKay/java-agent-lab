# ASM 

[ASM 官方文档](https://asm.ow2.io/index.html)


ASM provides three core components based on the ClassVisitor API to generate and transform classes:
- The `ClassReader` class parses a compiled class given as a byte array,
and calls the corresponding visitXxx methods on the ClassVisitor
instance passed as argument to its accept method. It can be seen as an
event producer.
- The `ClassWriter` class is a subclass of the ClassVisitor abstract class
that builds compiled classes directly in binary form. It produces as
output a byte array containing the compiled class, which can be retrieved
with the toByteArray method. It can be seen as an event consumer.
- The `ClassVisitor` class delegates all the method calls it receives to
another ClassVisitor instance. It can be seen as an event filter.