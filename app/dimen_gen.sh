#!/usr/bin/env bash
show_usage() {
    echo "usage: dimen_gen.sh path/base_dimens.xml"
}

if [ "$#" -ne 1 ]; then
    show_usage
    exit 0
fi

if [ ! -f "$1" ]; then
    echo "file not found."
    show_usage
    exit 0;
fi

#add supported dp here
support_dp_arr=(480 600 720 800 920 1080)
root=`pwd`
file_name=dimens.xml
default_space="    "

base_dp=`echo $1 | tr -d '[A-Za-z\/\-\.]'`
base_file=$1

for ((i=0; i < ${#support_dp_arr[@]}; i++))
do
  {
    support_dp=${support_dp_arr[$i]}
    if [ $base_dp -eq $support_dp ]; then
        echo "same as base" > /dev/null
        continue
    fi
    folder=$(echo $1 | sed "s/${base_dp}/${support_dp}/" | awk -F"${file_name}" '{ print $1 }')
    [ -d $folder ] && echo "${folder} found" > /dev/null || mkdir -p $folder
    cd $folder > /dev/null
    [ -f $file_name ] && rm $file_name
    while read -r line || [ -n "$line" ]
    do
        factor=`awk '{printf "%03.2f\n",  '${support_dp}'/'${base_dp}'}' <<< ""`
        if [[ ($line = *"dp"*) || ($line = *"sp"*) ]]; then
          cur=`echo $line | awk -F'>' '{print $2}' | sed "s/dp.*\|sp.*//g"`
          new=`echo "scale=2; $cur * $factor" | bc |sed -E 's/(^|[^0-9])\./\10./g'`
          out=`echo $line | sed "s/${cur}/${new}/g"`
          echo "$default_space$out" >> $file_name
        else
          echo $line >> $file_name
        fi
    done < "$root/$base_file"
    cd - > /dev/null
  } &
done
wait
