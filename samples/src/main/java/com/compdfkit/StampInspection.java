//  Copyright © 2014-2023 PDF Technologies, Inc. All Rights Reserved.
//
//  THIS SOURCE CODE AND ANY ACCOMPANYING DOCUMENTATION ARE PROTECTED BY INTERNATIONAL COPYRIGHT LAW
//  AND MAY NOT BE RESOLD OR REDISTRIBUTED. USAGE IS BOUND TO THE ComPDFKit LICENSE AGREEMENT.
//  UNAUTHORIZED REPRODUCTION OR DISTRIBUTION IS SUBJECT TO CIVIL AND CRIMINAL PENALTIES.
//  This notice may not be removed from this file.

package com.compdfkit;


import com.compdfkit.client.CPDFClient;
import com.compdfkit.constant.CPDFConstant;
import com.compdfkit.constant.CPDFLanguageConstant;
import com.compdfkit.enums.CPDFDocumentAIEnum;
import com.compdfkit.param.CPDFStampInspectionParameter;
import com.compdfkit.pojo.comPdfKit.CPDFCreateTaskResult;
import com.compdfkit.pojo.comPdfKit.CPDFFileInfo;
import com.compdfkit.pojo.comPdfKit.CPDFTaskInfoResult;
import com.compdfkit.pojo.comPdfKit.CPDFUploadFileResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class StampInspection {

    private static final String publicKey = "";
    private static final String secretKey = "";
    private static final CPDFClient client = new CPDFClient(publicKey,secretKey);

    public static void main(String[] args) throws FileNotFoundException {
        StampInspection.stampInspection();
    }

    public static void stampInspection() throws FileNotFoundException {
        // create Task
        CPDFCreateTaskResult createTaskResult = client.createTask(CPDFDocumentAIEnum.DETECTIONSTAMP, CPDFLanguageConstant.English);
        // taskId
        String taskId = createTaskResult.getTaskId();
        // upload File
        File file = new File("sample/test.jpg");
        String filePassword = "";
        CPDFStampInspectionParameter fileParameter = new CPDFStampInspectionParameter();
        CPDFUploadFileResult uploadFileResult = client.uploadFile(new FileInputStream(file),taskId,filePassword,fileParameter,file.getName(), CPDFLanguageConstant.English);
        String fileKey = uploadFileResult.getFileKey();
        // perform tasks
        client.executeTask(taskId, CPDFLanguageConstant.English);
        // get task processing information
        CPDFTaskInfoResult taskInfo = client.getTaskInfo(taskId);
        // determine whether the task status is "TaskFinish"
        if (taskInfo.getTaskStatus().equals(CPDFConstant.TASK_FINISH)) {
            System.out.println(taskInfo);
            // get the final file processing information
            CPDFFileInfo fileInfo = client.getFileInfo(fileKey);
            System.out.println(fileInfo);
            // if the task is finished, cancel the scheduled task
        }else {
            System.out.println("Task incomplete processing");
        }
    }

}
