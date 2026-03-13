LOC 0
LDR 3,0,ZERO       ; R3 = 0 at start
LDR 0,0,VAL1       ; R0 = 3
SRC 0,2,1,1        ; shift left logically by 2
LDR 2,0,EXP1       ; R2 = expected result 12
TRR 0,2            ; compare SRC result to expected
JCC 3,0,CHK2       ; if equal, continue to test 2
JMA 0,FAIL         ; otherwise fail
CHK2: LDR 0,0,VAL2 ; R0 = 12
SRC 0,2,0,1        ; shift right logically by 2
LDR 2,0,EXP2       ; R2 = expected result 3
TRR 0,2            ; compare SRC result to expected
JCC 3,0,CHK3       ; if equal, continue to test 3
JMA 0,FAIL         ; otherwise fail
CHK3: LDR 0,0,VAL3 ; R0 = -8
SRC 0,2,0,0        ; shift right arithmetically by 2
LDR 2,0,EXP3       ; R2 = expected result -2
TRR 0,2            ; compare SRC result to expected
JCC 3,0,PASS       ; if equal, pass
JMA 0,FAIL         ; otherwise fail
PASS: LDR 3,0,ONE  ; R3 = 1, SRC passed
HLT                ; stop on success
FAIL: HLT          ; stop on failure
VAL1: DATA 3       ; source for left logical shift
EXP1: DATA 12      ; expected 3 << 2
VAL2: DATA 12      ; source for right logical shift
EXP2: DATA 3       ; expected 12 >> 2 logical
VAL3: DATA -8      ; source for right arithmetic shift
EXP3: DATA -2      ; expected -8 >> 2 arithmetic
ZERO: DATA 0       ; constant 0
ONE: DATA 1        ; pass marker