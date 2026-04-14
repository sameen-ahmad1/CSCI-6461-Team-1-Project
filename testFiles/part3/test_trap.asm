LOC 0
Data 4              ; memory[0] = 4  (trap table lives at address 4)


LOC 4
Data 20             ; memory[4] = 20  (trap code 0 -> routine at address 20)


LOC 10
TRAP 0              ; reads memory[0]=4, reads memory[4+0]=20,
                    ; saves PC+1(=11) to memory[2], jumps to 20

STR 1, 0, 0, 28    ; memory[28] = R1  (should be 7 if routine ran)
STR 3, 0, 0, 29    ; memory[29] = R3  (should be 11 = return address)

JZ 0, 0, 0, 13     ; R0 == 0 always, spins here

LOC 20
LDA 1, 0, 0, 7     ; R1 = 7  (witness value: proves routine body executed)
LDR 3, 0, 0, 2     ; R3 = memory[2]  (should be 11, the saved return address)
JZ 0, 0, 1, 2      ; R0 == 0, I=1: jump to address stored in memory[2] = 11