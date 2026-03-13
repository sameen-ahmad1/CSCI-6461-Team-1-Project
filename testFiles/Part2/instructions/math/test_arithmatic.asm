LOC 0
LDX 1,BASE         ; X1 = 32, base of data page
LDR 3,1,0          ; R3 = 0, no tests passed yet

LDR 0,1,1          ; R0 = 2 for AMR test
AMR 0,1,2          ; R0 = 2 + 4 = 6
LDR 2,1,3          ; R2 = expected AMR result 6
TRR 0,2            ; compare AMR result to expected
JCC 3,0,AMR_PASS   ; if equal, AMR passed
JMA 0,HALT         ; otherwise fail
AMR_PASS: AIR 3,1  ; R3 = 1, AMR passed

LDR 0,1,4          ; R0 = 9 for SMR test
SMR 0,1,5          ; R0 = 9 - 3 = 6
LDR 2,1,3          ; R2 = expected SMR result 6
TRR 0,2            ; compare SMR result to expected
JCC 3,0,SMR_PASS   ; if equal, SMR passed
JMA 0,HALT         ; otherwise fail
SMR_PASS: AIR 3,1  ; R3 = 2, SMR passed

LDR 0,1,0          ; R0 = 0 for AIR test
AIR 0,5            ; R0 = 5, tests AIR with c(r)=0
LDR 2,1,6          ; R2 = expected AIR result 5
TRR 0,2            ; compare AIR result to expected
JCC 3,0,AIR_PASS   ; if equal, AIR passed
JMA 0,HALT         ; otherwise fail
AIR_PASS: AIR 3,1  ; R3 = 3, AIR passed

LDR 0,1,0          ; R0 = 0 for SIR test
SIR 0,4            ; R0 = -4, tests SIR with c(r)=0
LDR 2,1,7          ; R2 = expected SIR result -4
TRR 0,2            ; compare SIR result to expected
JCC 3,0,PASS       ; if equal, SIR passed
JMA 0,HALT         ; otherwise fail
PASS: AIR 3,1      ; R3 = 4, all arithmetic tests passed
HALT: HLT          ; stop execution
BASE: DATA 32      ; address of data page

LOC 32
DATA 0             ; offset 0: zero
DATA 2             ; offset 1: AMR initial register value
DATA 4             ; offset 2: AMR memory operand
DATA 6             ; offset 3: expected result for AMR and SMR
DATA 9             ; offset 4: SMR initial register value
DATA 3             ; offset 5: SMR memory operand
DATA 5             ; offset 6: expected result for AIR
DATA -4            ; offset 7: expected result for SIR