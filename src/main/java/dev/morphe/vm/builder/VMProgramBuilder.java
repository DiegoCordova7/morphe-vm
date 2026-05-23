package dev.morphe.vm.builder;

import dev.morphe.vm.core.VMHeap;
import dev.morphe.vm.core.types.IVMValue;
import dev.morphe.vm.exception.builder.UndefinedLabelException;
import dev.morphe.vm.instructions.Instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
	* Fluent builder for constructing a virtual machine program.
	* <p>
	* Provides convenient methods for emitting instructions corresponding to
	* stack operations, arithmetic, comparison, logical, control flow, arrays, and IO.
	* Each method returns the builder itself for fluent chaining.
	* </p>
	* <p>
	* Uses {@link ProgramState} internally to maintain heap, instructions,
	* labels, unresolved jumps, and the identifier pool.
	* </p>
	*/
public final class VMProgramBuilder {

	private final ProgramState state;

	private final StackBuilder stack;
	private final ScopeBuilder scope;
	private final ArithmeticBuilder arithmetic;
	private final ComparisonBuilder comparison;
	private final LogicalBuilder logical;
	private final ControlFlowBuilder control;
	private final ArrayBuilder array;
	private final IOBuilder io;

	/**
		* Creates a new program builder for the given heap.
		*
		* @param heap the VM heap used to store literals and values
		*/
	public VMProgramBuilder(VMHeap heap) {
		state = new ProgramState(heap);
		InstructionEmitter emitter = new InstructionEmitter(state);
		stack = new StackBuilder(emitter, state);
		scope = new ScopeBuilder(emitter, state);
		arithmetic = new ArithmeticBuilder(emitter);
		comparison = new ComparisonBuilder(emitter);
		logical = new LogicalBuilder(emitter);
		control = new ControlFlowBuilder(emitter, state);
		array = new ArrayBuilder(emitter);
		io = new IOBuilder(emitter);
	}

	/**
		* Helper to execute an emission action and return this builder.
		*
		* @return this builder for chaining
		*/
	private VMProgramBuilder emit(Runnable action) {
		action.run();
		return this;
	}

	/**
		* Pushes a heap-allocated value onto the stack by index.
		*
		* @param index the index of the value in the VM heap
		* @return this builder for chaining
		*/
	public VMProgramBuilder push(int index) {
		return emit(() -> stack.push(index));
	}

	/**
		* Pushes a literal value onto the stack.
		*
		* @param value the value to push
		* @return this builder for chaining
		*/
	public VMProgramBuilder pushLiteral(IVMValue value) {
		return emit(() -> stack.pushLiteral(value));
	}

	/**
		* Pops the top value from the stack.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder pop() {
		return emit(stack::pop);
	}

	/**
		* Duplicates the top value on the stack.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder dup() {
		return emit(stack::dup);
	}

	/**
		* Emits a STORE instruction to store the top of the stack into the given variable name.
		*
		* @param name the variable name
		* @return this builder for chaining
		*/
	public VMProgramBuilder store(String name) {
		return emit(() -> scope.store(name));
	}

	/**
		* Emits a LOAD instruction to push the value of the given variable name onto the stack.
		*
		* @param name the variable name
		* @return this builder for chaining
		*/
	public VMProgramBuilder load(String name) {
		return emit(() -> scope.load(name));
	}

	/**
		* Emits an ENTER_SCOPE instruction to create a new variable scope.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder enterScope() {
		return emit(scope::enterScope);
	}

	/**
		* Emits an EXIT_SCOPE instruction to exit the current variable scope.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder exitScope() {
		return emit(scope::exitScope);
	}

	/**
		* Emits an ADD instruction to add the top two values on the stack.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder add() {
		return emit(arithmetic::add);
	}

	/**
		* Emits a SUB instruction to subtract the top value from the second top value on the stack.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder sub() {
		return emit(arithmetic::sub);
	}

	/**
		* Emits a MUL instruction to multiply the top two values on the stack.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder mul() {
		return emit(arithmetic::mul);
	}

	/**
		* Emits a DIV instruction to divide the second top value by the top value on the stack.
		* <p>
		* Throws a runtime error in the VM if the divisor is zero.
		* </p>
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder div() {
		return emit(arithmetic::div);
	}

	/**
		* Emits a MOD instruction to compute the modulo of the second top value by the top value on the stack.
		* <p>
		* Throws a runtime error in the VM if the divisor is zero.
		* </p>
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder mod() {
		return emit(arithmetic::mod);
	}

	/**
		* Emits an EQ instruction to check if the two operands are equal.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder eq() {
		return emit(comparison::eq);
	}

	/**
		* Emits a NEQ instruction to check if the two operands are not equal.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder neq() {
		return emit(comparison::neq);
	}

	/**
		* Emits a LT instruction to check if the first operand is less than the second.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder lt() {
		return emit(comparison::lt);
	}

	/**
		* Emits a LTE instruction to check if the first operand is less than or equal to the second.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder lte() {
		return emit(comparison::lte);
	}

	/**
		* Emits a GT instruction to check if the first operand is greater than the second.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder gt() {
		return emit(comparison::gt);
	}

	/**
		* Emits a GTE instruction to check if the first operand is greater than or equal to the second.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder gte() {
		return emit(comparison::gte);
	}

	/**
		* Emits a NOT instruction to perform logical NOT on the top boolean value of the stack.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder not() {
		return emit(logical::not);
	}

	/**
		* Emits an OR instruction to perform logical OR on the top two boolean values of the stack.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder or() {
		return emit(logical::or);
	}

	/**
		* Emits an AND instruction to perform logical AND on the top two boolean values of the stack.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder and() {
		return emit(logical::and);
	}

	/**
		* Emits an if-else construct.
		*
		* @param condition the condition block that evaluates to a boolean on the stack
		* @param ifBlock   the block to execute if the condition is true
		* @param elseBlock optional block to execute if the condition is false
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder ifElse(Runnable condition, Runnable ifBlock, Runnable... elseBlock) {
		return emit(() -> control.ifElse(condition, ifBlock, elseBlock));
	}

	/**
		* Emits a match-case structure.
		*
		* @param expression the expression to evaluate
		* @param consumer   a consumer that receives a {@link ControlFlowBuilder.MatchBuilder} to define cases
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder match(Runnable expression, ControlFlowBuilder.MatchConsumer consumer) {
		return emit(() -> control.match(expression, consumer));
	}

	/**
		* Emits a while loop.
		* <p>
		* Execution flow:
		* <ul>
		*   <li>Evaluates the condition</li>
		*   <li>If false, jumps to the end</li>
		*   <li>Executes the body</li>
		*   <li>Jumps back to the condition</li>
		* </ul>
		*
		* @param condition the loop condition (must leave a boolean on the stack)
		* @param body      the loop body
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder whileLoop(Runnable condition, Runnable body) {
		return emit(() -> control.whileLoop(condition, body));
	}

	/**
		* Emits a do-while loop.
		* <p>
		* Execution flow:
		* <ul>
		*   <li>Executes the body</li>
		*   <li>Evaluates the condition</li>
		*   <li>If true, repeats</li>
		* </ul>
		*
		* @param body      the loop body
		* @param condition the loop condition (must leave a boolean on the stack)
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder doWhileLoop(Runnable condition, Runnable body) {
		return emit(() -> control.doWhileLoop(condition, body));
	}

	/**
		* Emits a for loop.
		* <p>
		* Execution flow:
		* <ul>
		*   <li>Executes initialization (if present)</li>
		*   <li>Evaluates condition (if present)</li>
		*   <li>If false, exits loop</li>
		*   <li>Executes body</li>
		*   <li>Executes increment (if present)</li>
		*   <li>Repeats</li>
		* </ul>
		*
		* @param init      optional initialization block
		* @param condition optional condition block (must leave a boolean on the stack)
		* @param increment optional increment block
		* @param body      the loop body
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder forLoop(Runnable init, Runnable condition, Runnable increment, Runnable body) {
		return emit(() -> control.forLoop(init, condition, increment, body));
	}

	/**
		* Emits an ARRAY_NEW instruction to create a new array.
		* <p>
		* The size must be provided on the stack before calling this instruction.
		* The resulting array index is pushed onto the stack.
		* </p>
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder newArray() {
		return emit(array::newArray);
	}

	/**
		* Emits an ARRAY_GET instruction to retrieve an element from an array.
		* <p>
		* Pops the array index and element index from the stack, then pushes
		* the element's heap index onto the stack.
		* </p>
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder arrayGet() {
		return emit(array::get);
	}

	/**
		* Emits an ARRAY_SET instruction to set an element in an array.
		* <p>
		* Pops the value index, element index, and array index from the stack
		* and updates the array.
		* </p>
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder arraySet() {
		return emit(array::set);
	}

	/**
		* Emits an ARRAY_LENGTH instruction to get the length of an array.
		* <p>
		* Pops the array index from the stack and pushes the length as a VMInteger.
		* </p>
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder arrayLength() {
		return emit(array::length);
	}

	/**
		* Emits a PRINT instruction to output the top value of the stack.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder print() {
		return emit(io::print);
	}

	/**
		* Emits a READ instruction to read a value from input and push it onto the stack.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder read() {
		return emit(io::read);
	}

	/**
		* Emits an INPUT instruction to read a line of input as a string and push it onto the stack.
		*
		* @return this builder for chaining
		*/
	public VMProgramBuilder input() {
		return emit(io::input);
	}

	/**
		* Resolves all labels and unresolved jumps, emits a final HALT instruction,
		* and returns the finalized list of VM instructions.
		*
		* @return this builder for chaining
		* @throws UndefinedLabelException  if a jump references an undefined label
		*/
	public List<Instruction> build() {
		for (Map.Entry<Integer, String> entry : state.unresolvedJumps.entrySet()) {
			int instrIndex = entry.getKey();
			String label = entry.getValue();
			Integer target = state.labels.get(label);
			if (target == null) throw new UndefinedLabelException(label);
			state.instructions.get(instrIndex).getOperands()[0] = target;
		}

		emit(control::halt);
		return new ArrayList<>(state.instructions);
	}
}