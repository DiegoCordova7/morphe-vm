# Eidos VM

Eidos VM is a lightweight stack-based virtual machine designed to execute programs compiled from the Eidos language. It provides a modular instruction system, a heap-based memory model, and a fluent builder API for program construction.

## Features

* Stack-based execution model
* Heap-managed values (`VMInteger`, `VMDouble`, `VMString`, `VMBoolean`, `VMArray`)
* Arithmetic, logical, comparison, and control flow instructions
* Structured programming support:

    * `if-else`
    * `while`, `do-while`, `for`
    * `match-case` (including ranges and lists)
* Scope management (variables with nested scopes)
* Built-in I/O operations
* Error propagation using `VMResult`
* Fluent `VMProgramBuilder` API

---

## Installation

Add the VM to your project:

```gradle
dependencies {
    implementation files('libs/vm-1.0.0.jar')
}
```

> Optionally attach `-sources.jar` and `-javadoc.jar` in your IDE for better navigation and documentation.

---

## 🚀 Quick Example

```java
VMHeap heap = new VMHeap(1024);
VMStack stack = new VMStack(1024);

VMProgramBuilder builder = new VMProgramBuilder(heap);

builder
    .pushLiteral(new VMInteger(5))
    .pushLiteral(new VMInteger(3))
    .add()
    .print();

List<Instruction> program = builder.build();

VM vm = new VM(heap, stack, program);
vm.execute();
```

---

## Architecture Overview

### Core Components

* **VM** → Executes instructions
* **VMHeap** → Stores all runtime values
* **Stack** → Holds references (heap indices)
* **Instruction** → Represents a single operation
* **IOpCodeAction** → Defines instruction behavior

---

### Instruction Categories

* **Arithmetic** → `ADD`, `SUB`, `MUL`, `DIV`, `MOD`
* **Logical** → `AND`, `OR`, `NOT`
* **Comparison** → `EQ`, `NEQ`, `GT`, `LT`, `GTE`, `LTE`
* **Control Flow** → `JMP`, `JMP_IF_TRUE`, `JMP_IF_FALSE`, `HALT`
* **Stack** → `PUSH`, `POP`, `DUP`
* **Scope** → `STORE`, `LOAD`, `ENTER_SCOPE`, `EXIT_SCOPE`
* **Array** → `ARRAY_NEW`, `ARRAY_GET`, `ARRAY_SET`, `ARRAY_LENGTH`
* **IO** → `PRINT`, `READ`, `INPUT`

---

## Program Builder

The `VMProgramBuilder` provides a fluent API to construct programs:

```java
builder
    .pushLiteral(new VMInteger(10))
    .store("x")
    .ifElse(
        () -> builder.load("x").pushLiteral(5).gt()
        () -> builder.load("x").print(),
        () -> builder.pushLiteral("Nope").print()
    );
```

---

## Match Example

```java
builder.match(
    () -> builder.load("value"),
    m -> m
        .caseVal(new VMInteger(1), () -> builder.print())
        .caseRange(new VMInteger(2), new VMInteger(5), () -> builder.print())
        .defaultCase(() -> builder.pushLiteral("default").print())
);
```

---

## Error Handling

Errors are handled using `VMResult` and propagated through the stack.

* Invalid operations do not crash the VM
* Errors are pushed as values and can be handled downstream

---

## Documentation

* Attach `vm-1.0.0-javadoc.jar` in your IDE to view full API documentation
* Attach `vm-1.0.0-sources.jar` to navigate source code

---

## Development

Built with:

* Java
* Gradle

To build:

```bash
./gradlew build
```

---

## License

This project is licensed under the Apache License 2.0.
See the LICENSE file for details.

---

## Notes

This VM is designed as part of the Eidos language ecosystem and may evolve alongside the compiler and type system.

---

## Future Improvements

* Bytecode serialization
* Debugger support
* Optimizations (constant folding, peephole)
* Standard library extensions
