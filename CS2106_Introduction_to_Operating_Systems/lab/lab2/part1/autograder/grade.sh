#!/bin/bash

echo "This is the script you have to write."
echo "Remember not to hardcode things!"

# Check if we have enough arguments
if [[ $# -ne 1 ]]; then
    echo "Usage: ./grade.sh <MAXSCORE>"
    exit -1
fi

max_score=$1

# Compile the reference program
gcc ref/*.c -o ref/fun
rm -f ref/*.out
in_files=$(ls ref/ | grep ".in")
in_files_array=$in_files
max_files=0
# Generate reference output files
for in_file in ${in_files_array[@]}; do 
    ./ref/fun < ref/$in_file >> ref/"$in_file".out
    max_files=$((max_files+1))
done

if [[ $max_files -le $max_score ]]; then
    max_score=$max_files
fi

stud_dir=$(ls subs/)
stud_array=$stud_dir
rm results.out
echo -e "Test date and time: $(date "+%A, %d %B %Y, %H:%M:%S")\n" >> results.out
processed=0
for stud in ${stud_array[@]}; do
    res="Directory $stud"
    processed=$((processed+1))
    score=0
    mkdir temp
    gcc "subs/$stud/"*.c -o temp/fun
    if [[ $? -ne 0 ]] then
        echo -e "$res has a compile error" >> results.out
        echo -e "$res score $score/$max_score" >> results.out
        continue
    fi
    for in_file in ${in_files_array[@]}; do 
        ./temp/fun < ref/$in_file >> temp/"$in_file".out
        diff temp/$in_file.out ref/$in_file.out > /dev/null
        if [[ $? -eq 0 ]] then
            score=$((score+1))
        fi
    done
    if [[ $score -ge $max_score ]] then
        score=$max_score
    fi
    echo -e "$res score $score/$max_score" >> results.out
    rm -rf temp
done

rm -rf temp

echo -e "\nProcessed $processed files" >> results.out

# Remember to check maximum score given as argument, compared to the real number of test cases

# Now mark submissions

#
# Note: See Lab02Qn.pdf for format of output file. Marks will be deducted for missing elements.
#

# Iterate over every submission directory
    # Compile C code
    # Print compile error message to output file (if any)
    # Generate output from C code using *.in files in /ref
    # Compare with reference output files  and award 1 mark if they are identical
    # print score for student
# print total submissions marked.
