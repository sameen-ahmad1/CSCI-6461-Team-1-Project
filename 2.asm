LOC     0
        Data    6
        Data    6

        LOC     6
        Data    0           ; [6]  idx (general purpose index)
        Data    0           ; [7]  bufLen (total chars stored in flat buffer)
        Data    0           ; [8]  wordCount
        Data    0           ; [9]  foundFlag
        Data    0           ; [10] foundSent
        Data    0           ; [11] foundWord
        Data    0           ; [12] scratch (indirect address)
        Data    0           ; [13] wbufIdx / scratch2
        Data    0           ; [14] sentCount (periods seen so far in search)
        Data    50          ; [15] const_50
        Data    48          ; [16] const_48
        Data    32          ; [17] const_32 (space)
        Data    10          ; [18] const_10 (LF)
        Data    13          ; [19] const_13 (CR)
        Data    WordPrompt  ; [20] WordPrompt address (fix jump target)        
        Data    46          ; [21] const_46 '.'
        Data    33          ; [22] const_33 '!'
        Data    63          ; [23] const_63 '?'
        Data    500         ; [24] const_wbuf (word buffer base)
        Data    400         ; [25] const_buf  (flat text buffer base)

        Data    RdLp        ; [26]
        Data    RdChar      ; [27]
        Data    PrDone      ; [28]
        Data    PrChar      ; [29]
        Data    RdWord      ; [30]
        Data    SrLoop      ; [31]

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
        STR     0,0,6       ; idx = 0
        STR     0,0,7       ; bufLen = 0

        LDX     1,26        ; jump to WordPrompt instead of RdLp
        JMA     1,0

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
        LDX     1,28
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

        LDX     1,20        ; FIX: go to WordPrompt (not RdLp / RdWord)
        JMA     1,0

PrChar:
        LDR     0,0,6
        LDR     1,0,7
        TRR     0,1
        LDX     1,30
        JCC     3,1,0

        LDR     2,0,25
        AMR     2,0,6
        STR     2,0,12
        LDX     1,12
        LDR     0,1,0

        OUT     0,1

        LDR     0,0,6
        AIR     0,1
        STR     0,0,6

        LDR     1,0,21
        TRR     0,1
        LDX     1,29
        JCC     3,1,0

        LDR     1,0,22
        TRR     0,1
        LDX     1,29
        JCC     3,1,0

        LDR     1,0,23
        TRR     0,1
        LDX     1,29
        JCC     3,1,0

        LDX     1,29
        JMA     1,0

PrNewline:
        LDR     0,0,19
        OUT     0,1
        LDR     0,0,18
        OUT     0,1
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     1,29
        JMA     1,0

WordPrompt:
        ; print "Enter word:\r\n"
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
        STR     0,0,6       ; charIdx = 0

        LDX     1,30        ; RdWord (NOT RdLp, NOT WordPrompt loop)
        JMA     1,0

RdWord:
        IN      0,0
        LDR     1,0,19
        TRR     0,1
        LDX     1,28
        JCC     3,1,0

        LDR     1,0,18
        TRR     0,1
        LDX     1,28
        JCC     3,1,0

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
        LDX     1,31        ; SrLoop (start search instead of re-prompting)
        JMA     1,0

WNullTerm:
        LDR     2,0,24
        AMR     2,0,6
        STR     2,0,12
        LDX     1,12
        LDA     0,0,0
        STR     0,1,0

SrInit:
        LDA     0,0,0
        STR     0,0,6
        STR     0,0,9
        STR     0,0,14
        LDA     0,0,1
        STR     0,0,8

SrLoop:
        LDR     0,0,6
        LDR     1,0,7
        TRR     0,1
        LDX     1,28
        JCC     3,1,0

        LDR     2,0,25
        AMR     2,0,6
        STR     2,0,12
        LDX     1,12
        LDR     0,1,0
        STR     0,0,13

        LDR     1,0,21
        TRR     0,1
        LDX     1,28
        JCC     3,1,0

        LDR     1,0,22
        TRR     0,1
        LDX     1,28
        JCC     3,1,0

        LDR     1,0,23
        TRR     0,1
        LDX     1,28
        JCC     3,1,0

        LDR     1,0,17
        TRR     0,1
        LDX     1,29
        JCC     3,1,0

        LDA     0,0,0
        STR     0,0,13

CmpLoop:
        LDR     2,0,24
        AMR     2,0,13
        STR     2,0,12
        LDX     1,12
        LDR     1,1,0

        LDA     2,0,0
        TRR     1,2
        LDX     1,28
        JCC     3,1,0

        LDR     2,0,6
        AMR     2,0,13
        LDR     3,0,25
        AMR     3,2,0
        STR     3,0,12
        LDX     2,12
        LDR     0,2,0

        TRR     0,1
        LDX     1,31
        JCC     3,1,0

        LDR     0,0,13
        AIR     0,1
        STR     0,0,13
        LDX     1,28
        JMA     1,0

ChkBound:
        LDR     2,0,6
        AMR     2,0,13
        LDR     3,0,25
        AMR     3,2,0
        STR     3,0,12
        LDX     2,12
        LDR     0,2,0

        LDA     1,0,0
        TRR     0,1
        LDX     1,28
        JCC     3,1,0

SrNoMatch:
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     1,31
        JMA     1,0

SrSpace:
        LDR     0,0,8
        AIR     0,1
        STR     0,0,8
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     1,28
        JMA     1,0

SrEndSent:
        LDR     0,0,14
        AIR     0,1
        STR     0,0,14
        LDA     0,0,1
        STR     0,0,8
        LDR     0,0,6
        AIR     0,1
        STR     0,0,6
        LDX     1,28
        JMA     1,0

Found:
        LDA     0,0,1
        STR     0,0,9
        LDR     0,0,14
        AIR     0,1
        STR     0,0,10
        LDR     0,0,8
        STR     0,0,11

PrintResult:
        LDR     0,0,9
        LDA     1,0,0
        TRR     0,1
        LDX     1,28
        JCC     3,1,0

NotFound:
        LDR     0,0,16
        AIR     0,30
        OUT     0,1
        HLT