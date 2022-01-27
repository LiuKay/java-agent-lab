# ByteBuddy Notes

## basics

### redefine

When redefining a class, Byte Buddy allows for the alteration of an existing class, either by adding fields and methods or by replacing existing method implementations. Preexisting method implementations are however lost if they are replaced by another implementation.

### rebase

When rebasing a class, Byte Buddy retains any method implementations of the rebased class. Instead of discarding overridden methods like when performing a *type redefinition*, Byte Buddy copies all such method implementations into renamed private methods with compatible signatures. This way, no implementation is lost and rebased methods can continue to invoke original code by calling these renamed methods. 





### ClassLoader

- default, ByteBuddy create a ClassLoader as the child of a given class loader that already exists in the application, so all types of the application are visible to the dynamic type
- child-first class loader
- inject a type into an existent class loader

