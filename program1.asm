        LOC     0
        Data    RSign
        Data    TgRead
        Data    TAcc
        Data    TSign
        Data    AbsOK
        Data    TDoAcc

        LOC     6
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    10
        Data    48
        Data    13
        Data    45
        Data    20
        Data    32

        Data    RdLp
        Data    PrLp
        Data    TgLp
        Data    SrLp
        Data    Done

        Data    Stor
        Data    Sv
        Data    Neg
        Data    Dgt
        Data    Acc
        Data    TNeg
        Data    TSv
        Data    Update

        LOC     32
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0

        LOC     60
        LDA     0,0,0
        STR     0,0,6
        STR     0,0,7
        STR     0,0,8

RdLp:
        LDR     0,0,6
        LDR     1,0,17
        TRR     0,1
        LDX     1,20
        JCC     3,1,0
        IN      0,0
        LDR     1,0,15
        TRR     0,1
        LDX     1,24
        JCC     3,1,0
        LDR     1,0,16
        TRR     0,1
        LDX     1,0
        JCC     3,1,0
        LDX     1,27
        JMA     1,0

RSign:
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
        LDX     1,3
        JCC     3,1,0
        LDX     1,2
        JMA     1,0

TSign:
        LDA     1,0,1
        STR     1,0,7
        LDX     1,1
        JMA     1,0

TAcc:
        SMR     0,0,14
        LDX     1,5
        JGE     0,1,0
        LDX     1,1
        JMA     1,0

TDoAcc:
        STR     0,0,12
        LDR     0,0,8
        LDR     2,0,13
        LDA     1,0,0
        MLT     0,2
        AMR     1,0,12
        STR     1,0,8
        LDX     1,1
        JMA     1,0

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
        LDX     1,30
        JMA     1,0

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
        LDX     1,4
        JGE     1,1,0
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