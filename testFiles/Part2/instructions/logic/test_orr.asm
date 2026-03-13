LOC 0
LDR 3,0,ZERO       ; R3 = 0 at start
LDR 0,0,A          ; R0 = 12
LDR 1,0,B          ; R1 = 10
ORR 0,1            ; R0 = 12 OR 10
LDR 2,0,EXP        ; R2 = expected result 14
TRR 0,2            ; compare actual ORR result to expected
JCC 3,0,PASS       ; if equal, pass
JMA 0,FAIL         ; otherwise fail
PASS: LDR 3,0,ONE  ; R3 = 1, ORR passed
HLT                ; stop on success
FAIL: HLT          ; stop on failure
A: DATA 12         ; first operand
B: DATA 10         ; second operand
EXP: DATA 14       ; expected ORR result
ZERO: DATA 0       ; constant 0
ONE: DATA 1        ; pass marker