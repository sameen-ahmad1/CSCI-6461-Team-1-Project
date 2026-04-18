LOC     0
        Data    6
        Data    6

        LOC     6
        Data    0           ; [6]  idx
        Data    0           ; [7]  bufLen
        Data    0           ; [8]  wordCount
        Data    0           ; [9]  foundFlag
        Data    0           ; [10] foundSent
        Data    0           ; [11] foundWord
        Data    0           ; [12] scratch (pointer temp)
        Data    0           ; [13] wbufIdx / char scratch
        Data    0           ; [14] sentCount
        Data    ChkBound    ; [15]
        Data    48          ; [16] const_48
        Data    32          ; [17] const_32 (space)
        Data    10          ; [18] const_10 (LF)
        Data    13          ; [19] const_13 (CR)
        Data    WordPrompt  ; [20]
        Data    46          ; [21] const_46 '.'
        Data    NotFound    ; [22]
        Data    PrintResult ; [23]
        Data    500         ; [24] const_wbuf
        Data    400         ; [25] const_buf
        Data    RdLp        ; [26]
        Data    SrLoop      ; [27]
        Data    PrDone      ; [28]
        Data    RdWord      ; [29]
        Data    CmpLoop     ; [30]
        Data    SrNoMatch   ; [31]

        LOC     400
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

        LOC     500
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
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0
        Data    0

LOC     600

Init:
        LDA     0,0,0
        STR     0,0,6
        STR     0,0,7

RdLp:
RdChar:
        CHK     0,2
        LDA     1,0,0
        TRR     0,1
        LDX     1,28
        JCC     3,1,0

        IN      0,2

        LDA     1,0,0
        TRR     0,1
        LDX     1,26
        JCC     3,1,0

        LDR     1,0,19
        TRR     0,1
        LDX     1,28
        JCC     3,1,0

        LDR     1,0,18
        TRR     0,1
        LDX     1,28
        JCC     3,1,0

        STR     0,0,13
        LDR     2,0,25
        AMR     2,0,6
        STR     2,0,12
        LDX     1,12
        LDR     0,0,13
        STR     0,1,0

        OUT     0,1

        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     1,26
        JMA     1,0

PrDone:
        LDR     0,0,6
        STR     0,0,7
        LDA     0,0,0
        STR     0,0,6
        LDX     1,20
        JMA     1,0

WordPrompt:
        LDR     0,0,19
        OUT     0,1
        LDR     0,0,18
        OUT     0,1
        LDR     0,0,16
        AIR     0,21
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,31
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,31
        AIR     0,6
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,22
        OUT     0,1
        AIR     0,13
        OUT     0,1
        LDR     0,0,17
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,31
        AIR     0,9
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,31
        AIR     0,1
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,31
        AIR     0,4
        OUT     0,1
        LDR     0,0,16
        AIR     0,31
        AIR     0,21
        OUT     0,1
        LDR     0,0,16
        AIR     0,10
        OUT     0,1
        LDR     0,0,19
        OUT     0,1
        LDR     0,0,18
        OUT     0,1

        LDA     0,0,0
        STR     0,0,6
        LDX     1,29
        JMA     1,0

RdWord:
        IN      0,0

        LDR     1,0,19
        TRR     0,1
        LDX     1,30
        JCC     3,1,0

        LDR     1,0,18
        TRR     0,1
        LDX     1,30
        JCC     3,1,0

        OUT     0,1

        STR     0,0,13
        LDR     2,0,24
        AMR     2,0,6
        STR     2,0,12
        LDX     1,12
        LDR     0,0,13
        STR     0,1,0

        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     1,29
        JMA     1,0

        ; CR or LF detected, null-terminate word buffer
        LDR     2,0,24
        AMR     2,0,6
        STR     2,0,12
        LDX     1,12
        LDA     0,0,0
        STR     0,1,0

        ; initialize search variables
        LDA     0,0,0
        STR     0,0,6
        STR     0,0,9
        STR     0,0,14
        LDA     0,0,1
        STR     0,0,8
        LDX     1,27
        JMA     1,0             ; → SrLoop

SrLoop:
        LDR     0,0,6
        LDR     1,0,7
        SMR     1,0,0
        LDX     2,23
        JCC     1,2,0           ; idx == bufLen → PrintResult

        ; load current char, save to address 13 (wbufIdx not needed yet)
        LDR     2,0,25
        AMR     2,0,6
        STR     2,0,12
        LDX     1,12
        LDR     0,1,0
        STR     0,0,13          ; save char to address 13

        ; if '.'
        LDR     0,0,13
        SMR     0,0,21          ; r0 = char - '.'
        LDX     2,27
        JNE     0,2,0           ; not '.', skip to space check
        LDR     0,0,14
        AIR     0,1
        STR     0,0,14
        LDA     0,0,1
        STR     0,0,8
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     2,27
        JMA     2,0             ; → SrLoop

        ; if space
        LDR     0,0,13
        SMR     0,0,17          ; r0 = char - space
        LDX     2,27
        JNE     0,2,0           ; not space, skip to word compare
        LDR     0,0,8
        AIR     0,1
        STR     0,0,8
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     2,27
        JMA     2,0             ; → SrLoop

        ; must be a word character, reset wbufIdx to 0
        LDA     0,0,0
        STR     0,0,13

CmpLoop:
        ; load word[wbufIdx] into r1
        LDR     2,0,24
        AMR     2,0,13
        STR     2,0,12
        LDX     1,12
        LDR     1,1,0

        ; if word[wbufIdx] == 0, fall through to ChkBound
        LDX     2,15
        JNE     1,2,0           ; if r1 != 0, jump past ChkBound to comparison

ChkBound:
        LDA     0,0,1
        STR     0,0,9
        LDR     0,0,14
        AIR     0,1
        STR     0,0,10
        LDR     0,0,8
        STR     0,0,11
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     2,23
        JMA     2,0             ; → PrintResult

        ; load text char and compare
        LDR     2,0,25
        AMR     2,0,6
        AMR     2,0,13
        STR     2,0,12
        LDX     2,12
        LDR     0,2,0

        ; mismatch → SrNoMatch
        TRR     0,1
        LDX     2,31
        JCC     3,2,0

        ; match: advance wbufIdx, jump back to CmpLoop
        LDR     0,0,13
        AIR     0,1
        STR     0,0,13
        LDX     2,30
        JMA     2,0             ; → CmpLoop

SrNoMatch:
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     2,27
        JMA     2,0             ; → SrLoop

PrintResult:
        LDR     0,0,9
        LDA     1,0,0
        TRR     0,1
        LDX     1,22
        JCC     3,1,0           ; foundFlag == 0 → NotFound

        ; move to fresh line
        LDR     0,0,19
        OUT     0,1
        LDR     0,0,18
        OUT     0,1

        ; print 'S'
        LDR     0,0,16
        AIR     0,31
        AIR     0,4
        OUT     0,1

        ; print ':'
        LDR     0,0,21
        AIR     0,12
        OUT     0,1

        ; print sentence number
        LDR     0,0,10
        AMR     0,0,16
        OUT     0,1

        ; print space
        LDR     0,0,17
        OUT     0,1

        ; print 'W'
        LDR     0,0,16
        AIR     0,31
        AIR     0,8
        OUT     0,1

        ; print ':'
        LDR     0,0,21
        AIR     0,12
        OUT     0,1

        ; print word number
        LDR     0,0,11
        AMR     0,0,16
        OUT     0,1

        LDR     0,0,18
        OUT     0,1
        LDR     0,0,19
        OUT     0,1

        HLT

NotFound:
        LDR     0,0,19
        OUT     0,1
        LDR     0,0,18
        OUT     0,1
        LDR     0,0,16
        AIR     0,30
        OUT     0,1
        HLT