package com.example.guozhaotong.demo.controller;

import com.example.guozhaotong.demo.entity.ResponseEntity;
import com.example.guozhaotong.demo.entity.ReturnStatus;
import com.example.guozhaotong.demo.repository.PersonRepository;
import com.example.guozhaotong.demo.repository.TimedTaskRepository;
import com.example.guozhaotong.demo.service.EtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Generated;


@RestController
public class EtlController2 {
    EtlService etlService = new EtlService();

    @Autowired
    PersonRepository personRepository;
    @Autowired
    TimedTaskRepository timedTaskRepository;

    /**
     * This is a test api
     *
     * @return
     */
    @PostMapping("/hi")
    public ResponseEntity hello() {
        return new ResponseEntity(ReturnStatus.OPERATION_SUCCESS.getStatusCode(), ReturnStatus.OPERATION_SUCCESS.getStatusMsg(),
                etlService.hello());
    }

    @GetMapping("/listPerson")
    public ResponseEntity listPerson() {
        return new ResponseEntity(ReturnStatus.OPERATION_SUCCESS.getStatusCode(), ReturnStatus.OPERATION_SUCCESS.getStatusMsg(),
                etlService.listPerson());
    }

    @PostMapping("/addrecord")
    public ResponseEntity addRecord(String name, double chinese, double math, double english) {
        return new ResponseEntity(ReturnStatus.ADD_DATA_SUCCESS.getStatusCode(), ReturnStatus.ADD_DATA_SUCCESS.getStatusMsg(),
                etlService.addRecord(name, chinese, math, english));
    }

    @GetMapping("/sortedbychinesedevidebypage")
    public ResponseEntity sortedByChinese(int page, int size) {
        return new ResponseEntity(ReturnStatus.OPERATION_SUCCESS.getStatusCode(), ReturnStatus.OPERATION_SUCCESS.getStatusMsg(),
                etlService.sortedByChinese(page, size));
    }

    @GetMapping("/sortedbychinese")
    public ResponseEntity sortedByChinese() {
        return new ResponseEntity(ReturnStatus.OPERATION_SUCCESS.getStatusCode(), ReturnStatus.OPERATION_SUCCESS.getStatusMsg(),
                etlService.sortedByChinese());
    }

    @Generated("/sortedbymath")
    public ResponseEntity sortedByMath() {
        return new ResponseEntity(ReturnStatus.OPERATION_SUCCESS.getStatusCode(), ReturnStatus.OPERATION_SUCCESS.getStatusMsg(),
                etlService.sortedByMath());
    }

    @GetMapping("/sortedbyenglish")
    public ResponseEntity sortedByEnglish() {
        return new ResponseEntity(ReturnStatus.OPERATION_SUCCESS.getStatusCode(), ReturnStatus.OPERATION_SUCCESS.getStatusMsg(),
                etlService.sortedByEnglish());
    }

    @PostMapping("/timedtask")
    public ResponseEntity timedTask(int year, int month, int day, int hour, int minute, int second, String taskName) {
        return new ResponseEntity(ReturnStatus.ADD_DATA_SUCCESS.getStatusCode(), ReturnStatus.ADD_DATA_SUCCESS.getStatusMsg(),
                etlService.timedTask(year, month, day, hour, minute, second, taskName));
    }

    @PostMapping("/deleteTimeTask")
    public ResponseEntity deleteTimeTask(long timeTaskId) {
        String res = etlService.deleteTimeTask(timeTaskId);
        switch (res){
            case "该任务不存在。":
            case "目前该任务已完成，无法取消执行任务。":
                return new ResponseEntity(ReturnStatus.DELETE_DATA_ERROR.getStatusCode(), ReturnStatus.DELETE_DATA_ERROR.getStatusMsg(), res);
            default: return new ResponseEntity(ReturnStatus.OPERATION_SUCCESS.getStatusCode(), ReturnStatus.OPERATION_SUCCESS.getStatusMsg(), res);
        }
    }

    @GetMapping("/listAllTimedTask")
    public ResponseEntity listAllTimedTask() {
        return new ResponseEntity(ReturnStatus.OPERATION_SUCCESS.getStatusCode(), ReturnStatus.OPERATION_SUCCESS.getStatusMsg(),
                etlService.listAllTimedTask());
    }

    @GetMapping("/listWaitingTimedTask")
    public ResponseEntity listWaitingTimedTask() {
        return new ResponseEntity(ReturnStatus.OPERATION_SUCCESS.getStatusCode(), ReturnStatus.OPERATION_SUCCESS.getStatusMsg(),
                etlService.listWaitingTimedTask());
    }

    @GetMapping("/listInterruptedTimedTask")
    public ResponseEntity listInterruptedTimedTask() {
        return new ResponseEntity(ReturnStatus.OPERATION_SUCCESS.getStatusCode(), ReturnStatus.OPERATION_SUCCESS.getStatusMsg(),
                etlService.listInterruptedTimedTask());
    }
}
