package dev.morphe.vm.builder;

import dev.morphe.vm.core.VMHeap;
import dev.morphe.vm.instructions.Instruction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the internal state while building a VM program.
 * <p>
 * Stores the VM heap, the list of instructions, and mappings for identifiers,
 * labels, and unresolved jumps. Used by the program builder to keep track
 * of the current compilation state and resolve references.
 * </p>
 */
public final class ProgramState {

  final VMHeap heap;
  final List<Instruction> instructions = new ArrayList<>();
  final Map<String,Integer> identifierPool = new HashMap<>();
  final Map<String,Integer> labels = new HashMap<>();
  final Map<Integer,String> unresolvedJumps = new HashMap<>();
  int labelCounter = 0;

  /**
   * Creates a new ProgramState with the given heap.
   *
   * @param heap the VM heap used for allocating program constants
   */
  ProgramState(VMHeap heap){
    this.heap = heap;
  }
}