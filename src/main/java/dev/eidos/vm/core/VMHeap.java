package dev.eidos.vm.core;

import dev.eidos.vm.core.types.IVMValue;

/**
	* Represents the heap memory of the VM.
	* <p>
	* The VMHeap manages allocation, deallocation, and access of values of type {@link IVMValue}.
	* Each allocated slot has a corresponding flag indicating whether it is in use.
	* </p>
	*/
public final class VMHeap {

	private final IVMValue[] heap;
	private final boolean[] used;

	/**
		* Creates a new VMHeap with the specified capacity.
		*
		* @param size The number of slots in the heap.
		*/
	public VMHeap(int size) {
		heap = new IVMValue[size];
		used = new boolean[size];
	}

	/**
		* Returns the total capacity of the heap.
		*
		* @return The total number of slots.
		*/
	public int capacity() {
		return heap.length;
	}

	/**
		* Returns the number of currently used slots.
		*
		* @return Number of allocated slots.
		*/
	public int used() {
		int count = 0;
		for (boolean b : used) if (b) count++;
		return count;
	}

	/**
		* Allocates a value in the heap and returns its index.
		*
		* @param value The value to allocate.
		* @return The heap index of the allocated value.
		* @throws RuntimeException if the heap is full.
		*/
	public int alloc(IVMValue value) {
		for (int i = 0; i < heap.length; i++) {
			if (!used[i]) {
				heap[i] = value;
				used[i] = true;
				return i;
			}
		}
		throw new RuntimeException("Heap full");
	}

	/**
		* Retrieves the value at the given heap index.
		*
		* @param index The heap index.
		* @return The value stored at the index.
		* @throws VMException if the index is invalid or the slot is free.
		*/
	public IVMValue get(int index) {
		if (index < 0 || index >= heap.length || !used[index])
			throw new VMException("Heap: invalid index " + index);
		return heap[index];
	}

	/**
		* Frees the value at the given heap index.
		*
		* @param index The heap index to free.
		* @throws VMException if the index is invalid or the slot is already free.
		*/
	public void free(int index) {
		if (index < 0 || index >= heap.length || !used[index])
			throw new VMException("Heap: invalid index " + index);
		heap[index] = null;
		used[index] = false;
	}

	/**
		* Allocates a temporary slot in the heap without a value.
		*
		* @return The index of the allocated temporary slot.
		* @throws VMException if the heap is full.
		*/
	public int allocTemp() {
		for (int i = 0; i < heap.length; i++) {
			if (!used[i]) {
				free(i);
				return i;
			}
		}
		throw new VMException("Heap full");
	}

	/**
		* Sets the value at an existing heap index.
		*
		* @param heapIndex The heap index.
		* @param value     The value to set.
		* @throws VMException if the index is invalid or the slot is not in use.
		*/
	public void set(int heapIndex, IVMValue value) {
		if (heapIndex < 0 || heapIndex >= heap.length || !used[heapIndex])
			throw new VMException("Heap: invalid index " + heapIndex);
		heap[heapIndex] = value;
	}
}