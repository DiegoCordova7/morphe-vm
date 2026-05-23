# Morphe VM

Morphe VM is a stack-based virtual machine with heap-managed runtime values and a structured instruction system. It is designed to execute programs compiled from the Morphe language through a custom IR pipeline.

The VM provides a modular instruction set, a heap-based memory model, and a fluent builder API for constructing and lowering high-level constructs into executable bytecode-like instructions.

---

## Features

- Stack-based execution model with heap-managed runtime values
- Heap-backed value system (`VMInteger`, `VMDouble`, `VMString`, `VMBoolean`, `VMArray`)
- Full instruction set including arithmetic, logical, comparison, and control-flow operations
- Structured programming support via lowered control-flow constructs:
  - `if-else`
  - `while`, `do-while`, `for`
  - `match-case` (including ranges and multi-value patterns)
- Lexical-style scope management with nested environments
- Built-in I/O abstraction layer
- Error handling using heap-allocated `VMResult` with explicit propagation semantics
- Fluent `VMProgramBuilder` for IR construction and control-flow generation

---

## Installation

The Morphe VM can be added as a local dependency in your project:

```gradle
dependencies {
    implementation files('libs/vm-1.0.0.jar')
}
```

> For a better development experience, it is recommended to also attach the -sources.jar and -javadoc.jar files in your IDE to enable source navigation and full API documentation support.

---

## 🚀 Quick Example

```java
VMHeap heap = new VMHeap(1024);
VMStack stack = new VMStack(1024);

VMProgramBuilder builder = new VMProgramBuilder(heap);

List<Instruction> program = builder
        .pushLiteral(new VMInteger(5))
        .pushLiteral(new VMInteger(3))
        .add()
        .print()
        .build();

VM vm = new VM(heap, stack, program);
vm.execute();
```

---

## Architecture Overview

### Core Components

- **VM** → Executes a linear instruction stream over a stack-based execution model
- **VMHeap** → Stores all runtime values and heap-allocated objects
- **VMStack** → Holds references (heap indices) used during execution
- **Instruction** → Represents a single low-level operation in the VM
- **IOpCodeAction** → Defines the execution behavior of each opcode

---

### Instruction System

The VM instruction set is organized into semantic groups:

- **Arithmetic** → `ADD`, `SUB`, `MUL`, `DIV`, `MOD`
- **Logical** → `AND`, `OR`, `NOT`
- **Comparison** → `EQ`, `NEQ`, `GT`, `LT`, `GTE`, `LTE`
- **Control Flow** → `JMP`, `JMP_IF_TRUE`, `JMP_IF_FALSE`, `HALT`
- **Stack Operations** → `PUSH`, `POP`, `DUP`
- **Scope Management** → `STORE`, `LOAD`, `ENTER_SCOPE`, `EXIT_SCOPE`
- **Heap Structures** → `ARRAY_NEW`, `ARRAY_GET`, `ARRAY_SET`, `ARRAY_LENGTH`
- **IO Operations** → `PRINT`, `READ`, `INPUT`

---

## Program Builder

The `VMProgramBuilder` provides a fluent API for constructing an intermediate representation (IR) of a program. It abstracts instruction emission, scope handling, and control-flow lowering into structured constructs.

```java
builder
    .pushLiteral(new VMInteger(10))
    .store("x")
    .ifElse(
        () -> builder.load("x").pushLiteral(5).gt(),
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

The VM uses heap-allocated `VMResult` values to represent runtime errors. Errors are treated as first-class values rather than exceptions.

Error propagation is handled explicitly during instruction execution:

- Invalid operations do not crash the VM
- Errors are stored in the heap as `VMResult` values
- Operations can detect and propagate errors before execution
- Error values can be consumed and handled downstream like regular values

---

## Development

This project is built with Java and Gradle.

To build the project:

```bash
./gradlew build
```

---

## License

This project is licensed under the Apache License 2.0.
See the LICENSE file for details.

---

## Notes

This VM is part of the Morphe language ecosystem and is designed to evolve alongside its compiler, type system, and IR generation pipeline.

Its architecture prioritizes:

- Explicit control over execution
- Extensibility of the instruction set
- Separation between IR generation and execution
- Deterministic runtime behavior
