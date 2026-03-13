LOC 0
LDR 3,0,ZERO       ; R3 = 0 at start
LDR 0,0,EQA        ; R0 = 25
LDR 1,0,EQB        ; R1 = 25
TRR 0,1            ; compare equal values
JCC 3,0,EQPASS     ; if equality bit set, equal-case passed
JMA 0,FAIL         ; otherwise fail
EQPASS: LDR 3,0,ONE ; R3 = 1, equal-case passed
LDR 0,0,NEQA       ; R0 = 25
LDR 1,0,NEQB       ; R1 = 9
TRR 0,1            ; compare unequal values
JCC 3,0,FAIL       ; if equality bit set here, fail
LDR 3,0,TWO        ; R3 = 2, not-equal case passed
HLT                ; stop on success
FAIL: HLT          ; stop on failure
EQA: DATA 25       ; equal test left operand
EQB: DATA 25       ; equal test right operand
NEQA: DATA 25      ; not-equal test left operand
NEQB: DATA 9       ; not-equal test right operand
ZERO: DATA 0       ; constant 0
ONE: DATA 1        ; first pass marker
TWO: DATA 2        ; second pass marker