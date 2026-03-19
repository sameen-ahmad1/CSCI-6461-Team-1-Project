; ================= DATA =================
        LOC     6
        Data    0       ; [6] count
        Data    0       ; [7] sign
        Data    0       ; [8] accum
        Data    0       ; [9] target
        Data    0       ; [10] bestdiff
        Data    0       ; [11] bestidx
        Data    0       ; [12] scratch
        Data    10      ; [13]
        Data    48      ; [14]
        Data    13      ; [15] CR
        Data    45      ; [16] '-'
        Data    20      ; [17]
        Data    32      ; [18] array base

        Data    RdLp    ; [19]
        Data    PrLp    ; [20]
        Data    TgLp    ; [21]
        Data    SrLp    ; [22]
        Data    Done    ; [23]

        Data    Stor    ; [24]
        Data    Sv      ; [25]
        Data    Neg     ; [26]
        Data    Dgt     ; [27]
        Data    Acc     ; [28]
        Data    TNeg    ; [29]
        Data    TSv     ; [30]
        Data    Update  ; [31]

; ===== ARRAY =====
        LOC     32
        Data    0,0,0,0,0,0,0,0,0,0
        Data    0,0,0,0,0,0,0,0,0,0

; ================= CODE =================
        LOC     60

; INIT
        LDA     0,0,0
        STR     0,0,6
        STR     0,0,7
        STR     0,0,8

; ================= READ =================
RdLp:
        LDR     0,0,6
        LDR     1,0,17
        TRR     0,1
        LDX     1,20
        JCC     3,1,0

        IN      0,0

; check CR
        LDR     1,0,15
        TRR     0,1
        LDX     1,24
        JCC     3,1,0

; check '-'
        LDR     1,0,16
        TRR     0,1
        LDX     1,27
        JCC     3,0,0     ; if NOT '-', go digit

; handle '-'
        LDA     1,0,1
        STR     1,0,7
        LDX     1,19
        JMA     1,0

Dgt:
        SMR     0,0,14
        LDX     1,28
        JGE     0,1,0

        LDX     1,19
        JMA     1,0

Acc:
        STR     0,0,12
        LDR     0,0,8
        LDR     2,0,13
        LDA     1,0,0
        MLT     0,2
        AMR     1,0,12
        STR     1,0,8

        LDX     1,19
        JMA     1,0

; ===== STORE =====
Stor:
        LDR     0,0,8
        LDR     1,0,7

        LDA     2,0,1
        TRR     1,2
        LDX     1,26
        JCC     3,1,0

        LDX     1,25
        JMA     1,0

Neg:
        NOT     0
        AIR     0,1

Sv:
        LDR     2,0,18
        AMR     2,0,6
        STR     2,0,12

        LDX     1,12
        STR     0,1,0

        LDR     0,0,6
        AIR     0,1
        STR     0,0,6

        LDA     0,0,0
        STR     0,0,7
        STR     0,0,8

        LDX     1,19
        JMA     1,0

; ================= PRINT =================
PrLp:
        LDR     0,0,6
        LDR     1,0,17
        TRR     0,1
        LDX     1,21
        JCC     3,1,0

        LDR     2,0,18
        AMR     2,0,6
        STR     2,0,12

        LDX     1,12
        LDR     0,1,0
        OUT     0,1

        LDR     0,0,6
        AIR     0,1
        STR     0,0,6

        LDX     1,20
        JMA     1,0

; ================= TARGET =================
TgLp:
        LDA     0,0,0
        STR     0,0,7
        STR     0,0,8

TgRead:
        IN      0,0

        LDR     1,0,15
        TRR     0,1
        LDX     1,30
        JCC     3,1,0

        LDR     1,0,16
        TRR     0,1
        LDX     1,TAcc
        JCC     3,0,0

        LDA     1,0,1
        STR     1,0,7
        JMA     0,0,TgRead

TAcc:
        SMR     0,0,14
        LDX     1,TgRead
        JGE     0,1,0

        STR     0,0,12
        LDR     0,0,8
        LDR     2,0,13
        LDA     1,0,0
        MLT     0,2
        AMR     1,0,12
        STR     1,0,8

        JMA     0,0,TgRead

TSv:
        LDR     0,0,8
        LDR     1,0,7

        LDA     2,0,1
        TRR     1,2
        LDX     1,29
        JCC     3,1,0

        STR     0,0,9

        LDA     0,0,0
        NOT     0
        SRC     0,1,0,1
        STR     0,0,10

        LDA     0,0,0
        STR     0,0,11
        STR     0,0,6

        LDX     1,22
        JMA     1,0

TNeg:
        NOT     0
        AIR     0,1
        JMA     0,0,TSv

; ================= SEARCH =================
SrLp:
        LDR     0,0,6
        LDR     1,0,17
        TRR     0,1
        LDX     1,23
        JCC     3,1,0

        LDR     2,0,18
        AMR     2,0,6
        STR     2,0,12

        LDX     1,12
        LDR     1,1,0

        SMR     1,0,9
        JGE     1,0,AbsOK
        NOT     1
        AIR     1,1

AbsOK:
        STR     1,0,12

        LDR     0,0,10
        SMR     0,0,12
        LDX     1,31
        JGE     0,1,0

        LDX     1,22
        JMA     1,0

Update:
        LDR     0,0,12
        STR     0,0,10
        LDR     0,0,6
        STR     0,0,11

        LDX     1,22
        JMA     1,0

; ================= DONE =================
Done:
        LDR     0,0,9
        OUT     0,1

        LDR     2,0,18
        AMR     2,0,11
        STR     2,0,12

        LDX     1,12
        LDR     0,1,0
        OUT     0,1

        HLT