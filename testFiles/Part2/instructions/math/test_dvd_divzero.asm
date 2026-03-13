LOC 0
LDR 3,0,ZERO       ; R3 = 0 at start
LDR 0,0,A          ; R0 = 6
LDR 2,0,ZERO       ; R2 = 0
DVD 0,2            ; divide by zero
JCC 2,0,PASS       ; if DIVZERO bit is set, pass
JMA 0,FAIL         ; otherwise fail
PASS: LDR 3,0,ONE  ; R3 = 1, divide-by-zero handling passed
HLT                ; stop on success
FAIL: HLT          ; stop on failure
A: DATA 6          ; dividend
ZERO: DATA 0       ; constant 0
ONE: DATA 1        ; pass marker