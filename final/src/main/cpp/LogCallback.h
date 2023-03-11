//
// Created by Steve on 2023/3/4.
//

#ifndef ANDROID_WAVEMAKER2_LOGCALLBACK_H
#define ANDROID_WAVEMAKER2_LOGCALLBACK_H
#include <vector>
#include <string>
#include <chrono>
using std::vector;
constexpr int ARRAY_SIZE=1000;

struct TimeAndCount{
    decltype(std::chrono::steady_clock::now()) time;
    int count;
};

class LogCallback {
public:
    LogCallback(const std::string name):name{name}{};
    bool add(const int cnt);
    void log();
private:
    vector<TimeAndCount> v{ARRAY_SIZE};
    int index=0;
    std::string name;
};


#endif //ANDROID_WAVEMAKER2_LOGCALLBACK_H
