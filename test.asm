; --- Test Program for C6461 Assembler ---
LOC 10          ; Start the program at memory address 10
Begin:  LDR 3,0,15    ; Load Register 3 with value at address 15
        STR 2,1,20,1  ; Store R2 using IX 1, Address 20, Indirect 1
        JZ  0,0,End   ; Jump to the "End" label if R0 is zero

LOC 20          ; Jump the location counter to address 20
Data    500           ; Store the number 500 in this memory slot
End:    HLT           ; Halt instruction (usually Opcode 0)
Data    Begin         ; Store the address of "Begin" (should be 000012 octal)