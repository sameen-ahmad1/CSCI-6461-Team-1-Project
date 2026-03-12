LOC 0
LDR 3,0,ZERO       ; R3 = 0 at start
LDR 0,0,A          ; R0 = 6
LDR 2,0,B          ; R2 = 7
MLT 0,2            ; R0:R1 = 6 * 7
LDR 2,0,ZERO       ; R2 = expected high half 0
TRR 0,2            ; compare R0 to expected high half
JCC 3,0,CHKLOW     ; if equal, check low half
JMA 0,FAIL         ; otherwise fail
CHKLOW: LDR 2,0,LOW ; R2 = expected low half 42
TRR 1,2            ; compare R1 to expected low half
JCC 3,0,PASS       ; if equal, pass
JMA 0,FAIL         ; otherwise fail
PASS: LDR 3,0,ONE  ; R3 = 1, MLT passed
HLT                ; stop on success
FAIL: HLT          ; stop on failure
A: DATA 6          ; multiplicand
B: DATA 7          ; multiplier
ZERO: DATA 0       ; constant 0
LOW: DATA 42       ; expected low half
ONE: DATA 1        ; pass marker