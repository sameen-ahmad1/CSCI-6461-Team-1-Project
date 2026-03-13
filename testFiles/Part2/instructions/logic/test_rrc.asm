LOC 0
LDR 3,0,ZERO       ; R3 = 0 at start
LDR 0,0,VAL1       ; R0 = 4097
RRC 0,4,1,1        ; rotate left by 4
LDR 2,0,EXP1       ; R2 = expected result 17
TRR 0,2            ; compare RRC result to expected
JCC 3,0,CHK2       ; if equal, continue to test 2
JMA 0,FAIL         ; otherwise fail
CHK2: LDR 0,0,VAL2 ; R0 = 17
RRC 0,4,0,1        ; rotate right by 4
LDR 2,0,EXP2       ; R2 = expected result 4097
TRR 0,2            ; compare RRC result to expected
JCC 3,0,CHK3       ; if equal, continue to test 3
JMA 0,FAIL         ; otherwise fail
CHK3: LDR 0,0,VAL3 ; R0 = 5
RRC 0,0,1,1        ; rotate left by 0, no change
LDR 2,0,EXP3       ; R2 = expected result 5
TRR 0,2            ; compare RRC result to expected
JCC 3,0,PASS       ; if equal, pass
JMA 0,FAIL         ; otherwise fail
PASS: LDR 3,0,ONE  ; R3 = 1, RRC passed
HLT                ; stop on success
FAIL: HLT          ; stop on failure
VAL1: DATA 4097    ; source for rotate-left test
EXP1: DATA 17      ; expected rotate-left result
VAL2: DATA 17      ; source for rotate-right test
EXP2: DATA 4097    ; expected rotate-right result
VAL3: DATA 5       ; source for count-0 test
EXP3: DATA 5       ; expected unchanged result
ZERO: DATA 0       ; constant 0
ONE: DATA 1        ; pass marker