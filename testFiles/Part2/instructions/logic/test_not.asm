LOC 0
LDR 3,0,ZERO       ; R3 = 0 at start
LDR 0,0,A          ; R0 = 15
NOT 0              ; R0 = NOT 15
LDR 2,0,EXP        ; R2 = expected result -16
TRR 0,2            ; compare actual NOT result to expected
JCC 3,0,PASS       ; if equal, pass
JMA 0,FAIL         ; otherwise fail
PASS: LDR 3,0,ONE  ; R3 = 1, NOT passed
HLT                ; stop on success
FAIL: HLT          ; stop on failure
A: DATA 15         ; operand
EXP: DATA -16      ; expected NOT result in 16-bit two's complement
ZERO: DATA 0       ; constant 0
ONE: DATA 1        ; pass marker