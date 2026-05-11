package dev.eidos.vm.core;

import dev.eidos.vm.core.types.IVMValue;

/**
	* Represents the heap memory used by the VM.
	* <p>
	* {@code VMHeap} manages allocation and access of values of type {@link IVMValue}.
	* Each allocated slot has a corresponding flag indicating whether it is currently
	* in use.
	* </p>
	*
	* <p>
	* The heap also tracks basic allocation metrics, such as the current number of
	* used slots and the highest number of used slots reached during execution.
	* </p>
	*/
public final class VMHeap {

	/** Stores heap values by index. */
	private final IVMValue[] heap;

	/** Tracks whether each heap slot is currently allocated. */
	private final boolean[] used;

	/** Current number of allocated heap slots. */
	private int usedCount;

	/** Highest number of allocated heap slots reached during execution. */
	private int peakUsed;

	/**
		* Creates a new VMHeap with the specified capacity.
		*
		* @param size the number of slots in the heap
		*/
	public VMHeap(int size) {
		heap = new IVMValue[size];
		used = new boolean[size];
	}

	/**
		* Returns the total capacity of the heap.
		*
		* @return total number of heap slots
		*/
	public int capacity() {
		return heap.length;
	}

	/**
		* Returns the current number of allocated heap slots.
		*
		* @return current heap usage
		*/
	public int used() {
		return usedCount;
	}

	/**
		* Returns the highest number of allocated heap slots reached so far.
		*
		* @return peak heap usage
		*/
	public int peakUsed() {
		return peakUsed;
	}

	/**
		* Allocates a value in the heap and returns its index.
		*
		* @param value the value to allocate
		* @return the heap index of the allocated value
		* @throws VMException if the heap is full
		*/
	public int alloc(IVMValue value) {
		for (int i = 0; i < heap.length; i++) {
			if (!used[i]) {
				heap[i] = value;
				used[i] = true;
				recordAllocation();
				return i;
			}
		}
		throw new VMException("Heap full");
	}

	/**
		* Allocates a temporary slot in the heap without assigning a value.
		* <p>
		* The slot is marked as used and initialized with {@code null}.
		* This is useful for intermediate VM operations that require reserving
		* heap space before assigning a concrete value.
		* </p>
		*
		* @return the allocated heap index
		* @throws VMException if the heap is full
		*/
	public int allocTemp() {
		for (int i = 0; i < heap.length; i++) {
			if (!used[i]) {
				heap[i] = null;
				used[i] = true;
				recordAllocation();
				return i;
			}
		}
		throw new VMException("Heap full");
	}

	/**
		* Updates heap allocation counters after a slot is allocated.
		*/
	private void recordAllocation() {
		usedCount++;
		if (usedCount > peakUsed) {
			peakUsed = usedCount;
		}
	}

	/**
		* Retrieves the value at the given heap index.
		*
		* @param index the heap index
		* @return the value stored at the given index
		* @throws VMException if the index is invalid or the slot is free
		*/
	public IVMValue get(int index) {
		if (index < 0 || index >= heap.length || !used[index]) {
			throw new VMException("Heap: invalid index " + index);
		}
		return heap[index];
	}

	/**
		* Sets the value at an existing heap index.
		*
		* @param heapIndex the heap index
		* @param value the value to set
		* @throws VMException if the index is invalid or the slot is not in use
		*/
	public void set(int heapIndex, IVMValue value) {
		if (heapIndex < 0 || heapIndex >= heap.length || !used[heapIndex]) {
			throw new VMException("Heap: invalid index " + heapIndex);
		}
		heap[heapIndex] = value;
	}
}