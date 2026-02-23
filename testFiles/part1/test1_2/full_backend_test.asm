; Full backend test (works with your assembler limits)
; Tests: LDR, STR, LDA, LDX, STX, indexing, indirect
; Address field is 5-bit => 0..31 only

LOC 0

; --- Basic LDR/STR ---
LDR 0,0,20     ; R0 = Mem[20] = 5
STR 0,0,24     ; Mem[24] = 5
LDR 1,0,24     ; R1 = Mem[24] = 5

; --- Set IX1 = 3 using LDX ---
LDR 2,0,21     ; R2 = Mem[21] = 3
STR 2,0,25     ; Mem[25] = 3
LDX 1,25       ; IX1 = Mem[25] = 3      (LDX x,address)

; --- Indexing test (BASE=26, so 26+IX1=29) ---
LDR 0,0,22     ; R0 = Mem[22] = 7
STR 0,1,26     ; Mem[26 + IX1] = 7  -> Mem[29]
LDR 3,1,26     ; R3 = Mem[26 + IX1] -> should be 7

; --- STX test ---
STX 1,30       ; Mem[30] = IX1 = 3
LDX 2,30       ; IX2 = Mem[30] = 3

; --- Indirect test ---
LDR 0,0,23     ; R0 = Mem[23] = 99
STR 0,0,18     ; Mem[18] = 99     (VAL at 18)
LDA 1,0,18     ; R1 = 18          (address of VAL)
STR 1,0,19     ; Mem[19] = 18     (PTR at 19)
LDR 2,0,19,1   ; R2 = Mem[ Mem[19] ] = Mem[18] = 99

HLT

; -------------------------
; Data section (all 0..31)
; -------------------------
LOC 18
DATA 0           ; [18] VAL (will become 99)
DATA 0           ; [19] PTR (will become 18)
DATA 5           ; [20] constant 5
DATA 3           ; [21] constant 3
DATA 7           ; [22] constant 7
DATA 99          ; [23] constant 99
DATA 0           ; [24] OUT1 (should become 5)
DATA 0           ; [25] TMP  (used for IX load)
DATA 0           ; [26] BASE
DATA 0           ; [27]
DATA 0           ; [28]
DATA 0           ; [29] BASE+3 (should become 7)
DATA 0           ; [30] IXSAVE (should become 3)
DATA 0           ; [31] unused