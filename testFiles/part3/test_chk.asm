LOC 10
CHK 0, 0            ; R0 <- keyboard status  (devid 0)
CHK 1, 1            ; R1 <- printer status   (devid 1)
CHK 2, 2            ; R2 <- card reader status (devid 2)

STR 0, 0, 0, 20    ; memory[20] = R0 (keyboard result)
STR 1, 0, 0, 21    ; memory[21] = R1 (printer result)
STR 2, 0, 0, 22    ; memory[22] = R2 (card reader result)

LOC 17
JZ 0, 0, 0, 17     ; R0 holds keyboard status (=1), so this WON'T spin
                    ; see note below

LOC 16
JZ 3, 0, 0, 16     ; R3 = 0 always, spins here