//
// Created by Steve on 2023/3/4.
//
#include <aaudio/AAudio.h>
#include <android/log.h>

#include "LogCallback.h"

bool LogCallback::add(const int cnt) {
    if(index>=ARRAY_SIZE) return false;

    TimeAndCount t={std::chrono::steady_clock::now(),cnt};
    v[index++]=t;
    if(index==ARRAY_SIZE) log();
    return true;
}
void LogCallback::log(){
    __android_log_print(ANDROID_LOG_ERROR, "LogCallback","%s",name.c_str());
    for(int i=1;i<ARRAY_SIZE;i++){
        auto dur =std::chrono::duration_cast<std::chrono::nanoseconds>(v[i].time-v[0].time).count();
        __android_log_print(ANDROID_LOG_ERROR, "LogCallback","%3d %5d : %lld : %lld",
                            i,v[i].count,
                            std::chrono::duration_cast<std::chrono::microseconds>(v[i].time-v[0].time).count(),
                            std::chrono::duration_cast<std::chrono::microseconds>(v[i].time-v[i-1].time).count()

                            //std::chrono::microseconds(v[i].time-v[0].time)
                            //(v[i].time-v[i-1].time).count()
                            );
    }
}