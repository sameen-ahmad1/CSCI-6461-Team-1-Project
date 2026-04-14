LOC     000                                                                                                                                                    
        Data    020         ; mem[0] = trap table base = 020 (octal)
                                                                                                                                                                        
        LOC     010                                                                                                                                                    
        TRAP    1           ; fire trap code 1                                                                                                                         
        HLT                 ; <-- TRAP should save PC here (011) to mem[2]                                                                                             
                                                                                                                                                                        
        LOC     020          
        Data    030         ; trap table[0] = unused                                                                                                                   
        Data    030         ; trap table[1] = handler at 030 (octal)
                                                                                                                                                                        
        LOC     030          
        HLT                 ; simple handler: just halt 