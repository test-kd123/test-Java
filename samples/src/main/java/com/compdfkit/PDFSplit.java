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
import com.compdfkit.enums.CPDFDocumentEditorEnum;
import com.compdfkit.param.CPDFPageSplitParameter;
import com.compdfkit.pojo.comPdfKit.CPDFCreateTaskResult;
import com.compdfkit.pojo.comPdfKit.CPDFFileInfo;
import com.compdfkit.pojo.comPdfKit.CPDFTaskInfoResult;
import com.compdfkit.pojo.comPdfKit.CPDFUploadFileResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class PDFSplit {

    private static final String publicKey = "";
    private static final String secretKey = "";
    private static final CPDFClient client = new CPDFClient(publicKey,secretKey);

    public static void main(String[] args) throws FileNotFoundException {
        PDFSplit.pageSplit();
    }

    public static void pageSplit() throws FileNotFoundException {
        // create Task
        CPDFCreateTaskResult createTaskResult = client.createTask(CPDFDocumentEditorEnum.SPLIT, CPDFLanguageConstant.ENGLISH);
        // taskId
        String taskId = createTaskResult.getTaskId();
        // upload File
        File file = new File("sample/test.pdf");
        String filePassword = "";
        CPDFPageSplitParameter fileParameter = new CPDFPageSplitParameter();
        fileParameter.setPageOptions(Arrays.asList("1","2"));
        CPDFUploadFileResult uploadFileResult = client.uploadFile(new FileInputStream(file),taskId,filePassword,fileParameter,file.getName(), CPDFLanguageConstant.ENGLISH);
        String fileKey = uploadFileResult.getFileKey();
        // perform tasks
        client.executeTask(taskId, CPDFLanguageConstant.ENGLISH);
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
