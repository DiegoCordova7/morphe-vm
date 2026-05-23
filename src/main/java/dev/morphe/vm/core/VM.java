package dev.morphe.vm.core;

import dev.morphe.vm.core.io.IVMIOHandler;
import dev.morphe.vm.core.metrics.VMMetrics;
import dev.morphe.vm.core.metrics.VMMetricsCollector;
import dev.morphe.vm.instructions.Instruction;

import java.util.List;

/**
	* The main Virtual Machine (VM) class responsible for executing a program.
	* <p>
	* The VM manages instruction execution, memory (heap and stack), variable scopes,
	* instruction pointer (IP), and input/output operations.
	* </p>
	*/
public final class VM {

	private final List<Instruction> program;
	private int ip = 0;
	private final VMStack stack;
	private final VMHeap heap;
	private boolean running;
	private VMScope currentScope;
	private IVMIOHandler ioHandler;
	private final VMMetricsCollector metricsCollector  = new VMMetricsCollector();
	private VMMetrics metrics;

	/**
		* Creates a new VM instance with the specified program, stack, and heap.
		*
		* @param program The list of instructions to execute.
		* @param stack   The VMStack to use for execution.
		* @param heap    The VMHeap to use for memory allocation.
		*/
	public VM(List<Instruction> program, VMStack stack, VMHeap heap) {
		this.program = program;
		this.stack = stack;
		this.heap = heap;
		this.currentScope = new VMScope(null);
		this.running = true;
	}

	/**
		* Runs the VM by executing instructions sequentially until completion
		* or until {@link #stop()} is called.
		*/
	public void run() {
		long startTime = System.nanoTime();
		metricsCollector.setInstructionCount(program.size());
		metricsCollector.recordHeapCapacity(heap.capacity());

		while (running && ip < program.size()) {
			Instruction instr = program.get(ip);
			metricsCollector.recordInstructionExecution();
			instr.getAction().execute(this, instr);
			metricsCollector.recordStackSize(stack.size());
			ip++;
		}

		metricsCollector.recordStackSize(stack.size());
		metricsCollector.recordUsedHeapSlots(heap.used());
		metricsCollector.recordPeakHeapUsage(heap.peakUsed());
		metricsCollector.recordRuntime(System.nanoTime() - startTime);
		metrics = metricsCollector.snapshot();
	}

	/**
		* Returns the collected VM execution metrics.
		* <p>
		* Returns {@code null} if the VM has not been executed yet.
		* </p>
		*
		* @return the VM metrics snapshot
		*/
	public VMMetrics getMetrics() {
		return metrics;
	}

	/**
		* Returns the I/O handler used by the VM.
		*
		* @return The current IVMIOHandler.
		*/
	public IVMIOHandler getIOHandler() {
		return ioHandler;
	}

	/**
		* Sets a custom I/O handler for the VM.
		*
		* @param ioHandler The IVMIOHandler to use.
		*/
	public void setIOHandler(IVMIOHandler ioHandler) {
		this.ioHandler = ioHandler;
	}

	/**
		* Returns the VM's stack.
		*
		* @return The VMStack instance.
		*/
	public VMStack getStack() {
		return stack;
	}

	/**
		* Returns the VM's heap.
		*
		* @return The VMHeap instance.
		*/
	public VMHeap getHeap() {
		return heap;
	}

	/**
		* Returns the current variable scope.
		*
		* @return The current VMScope.
		*/
	public VMScope getCurrentScope() {
		return currentScope;
	}

	/**
		* Sets the current variable scope.
		*
		* @param scope The VMScope to set as current.
		*/
	public void setCurrentScope(VMScope scope) {
		this.currentScope = scope;
	}

	/**
		* Sets the instruction pointer to the specified target index.
		*
		* @param target The instruction index to jump to.
		*/
	public void jump(int target) {
		this.ip = target;
	}

	/**
		* Stops the VM, terminating execution.
		*/
	public void stop() {
		this.running = false;
	}
}