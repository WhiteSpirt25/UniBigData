from multiprocessing import Pool
import os
import mmap
import struct
import time
        
def read_part(memmap):
    num_sum = 0
    max_num = 0
    min_num = 4_294_967_295
    
    for nnum in range(len(memmap)//4):
        
        number = struct.unpack('>I',memmap[nnum*4:(nnum+1)*4])[0]
        
        num_sum+=number
        if number>max_num:
            max_num = number
        if number<min_num:
            min_num = number

    return (num_sum,max_num,min_num)


if __name__ == '__main__':
    '''
    Программа маппит файл с помощью mmap и с помощью [:] разделяет его между процессами.
    Каждый процесс считает у себя сумму и min/max. Результаты процессов объёдиняются.
    
    Файл делится на примерно равные части, по одинаковому количеству числе в каждой.
    Избыток осатётся последнему процессу.
    '''
    start_time = time.time()
    
    # Имя читаемого файла
    filename = "numbers"
    num_of_workers = 4
    
    total_numbers = os.path.getsize(filename) // 4

    f = open(filename,"rb")


    # количество чисел для каждого процесса
    segment = total_numbers//num_of_workers
    # индексы для начала сегментов с которыми вместе соединяются длины сегментов, для передачи процессам
    split = list(range(0,segment*num_of_workers,segment))

    split = [(start*4,(start+segment)*4) for start in split]
    split[-1] = (split[-1][0],total_numbers*4)
    
    mm = mmap.mmap(f.fileno(), length=0, access=mmap.ACCESS_READ)
    mm_split = [mm[el[0]:el[1]] for el in split]
    

    with Pool(processes=num_of_workers) as pool:
        res = pool.map(read_part,mm_split)

    num_sum = 0
    max_num = 0
    min_num = 4_294_967_295

    for el in res:
        num_sum+= el[0]
        
        if el[1]>max_num:
            max_num = el[1]
            
        if el[2]<min_num:
            min_num = el[2]

    print(f'''
    Total sum: {num_sum}
    Max number: {max_num}
    Min number: {min_num}
    ''')
    
    print("--- %s seconds ---" % (time.time() - start_time))