LOC 0
LDR 3,0,ZERO       ; R3 = 0 at start
LDR 0,0,A          ; R0 = 20
LDR 2,0,B          ; R2 = 6
DVD 0,2            ; R0 = quotient, R1 = remainder
LDR 2,0,QUOT       ; R2 = expected quotient 3
TRR 0,2            ; compare quotient
JCC 3,0,CHKREM     ; if equal, check remainder
JMA 0,FAIL         ; otherwise fail
CHKREM: LDR 2,0,REM ; R2 = expected remainder 2
TRR 1,2            ; compare remainder
JCC 3,0,PASS       ; if equal, pass
JMA 0,FAIL         ; otherwise fail
PASS: LDR 3,0,ONE  ; R3 = 1, DVD passed
HLT                ; stop on success
FAIL: HLT          ; stop on failure
A: DATA 20         ; dividend
B: DATA 6          ; divisor
QUOT: DATA 3       ; expected quotient
REM: DATA 2        ; expected remainder
ZERO: DATA 0       ; constant 0
ONE: DATA 1        ; pass marker