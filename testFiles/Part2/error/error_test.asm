; --- VALID CODE TO START ---
        LDX 2, 15        ; Load Index Register 2 with value from M[15]
        LDR 0, 2, 10, 0  ; Load R0 from M[10 + IX2]

; --- TEST 1: DUPLICATE LABEL (Assembler Error) ---
; This should trigger "postAssemblerError" in the GUI
; MYLABEL: DATA 10
; MYLABEL: DATA 20         ; ERROR: Duplicate symbol 'MYLABEL'

; --- TEST 2: MISSING DATA VALUE (Assembler Error) ---
; BAD_DATA: DATA           ; ERROR: DATA instruction requires a value

; --- TEST 3: ILLEGAL OPCODE (Runtime Error) ---
; We force a bit pattern that doesn't map to any Instruction
; In Octal, 0177777 is usually all 1s.
;       .WORD 0177777    ; ERROR: CPU/Decoder should post "Illegal Opcode 77"

; --- TEST 4: ADDRESS OUT OF BOUNDS (Runtime Error) ---
; Trying to access memory beyond your 2048 limit
;        STR 1, 0, 3000, 0 ; ERROR: MemoryBus should post "Invalid Address 3000"

; --- TEST 5: INVALID REGISTER (Assembler/Decoder Error) ---
        LDR 5, 0, 10, 0   ; ERROR: Register 5 does not exist (only 0-3)

; --- TEST 6: CIRCULAR INDIRECT (Runtime Logic) ---
; M[30] points to 30. An indirect load here might cause a loop or fault.
        .SET 30, 30
        LDR 0, 0, 30, 1