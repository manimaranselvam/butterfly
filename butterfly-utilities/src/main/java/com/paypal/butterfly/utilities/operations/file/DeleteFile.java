package com.paypal.butterfly.utilities.operations.file;

import com.paypal.butterfly.extensions.api.TOExecutionResult;
import com.paypal.butterfly.extensions.api.TransformationContext;
import com.paypal.butterfly.extensions.api.TransformationOperation;
import com.paypal.butterfly.extensions.api.exception.TransformationUtilityException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Deletes a single file or folder (empty or not).
 *
 * @author facarvalho
 */
public class DeleteFile extends TransformationOperation<DeleteFile> {

    private static final String DESCRIPTION = "Delete file %s";

    /**
     * Deletes a single file or folder (empty or not).
     */
    public DeleteFile() {
    }

    @Override
    public String getDescription() {
        return String.format(DESCRIPTION, getRelativePath());
    }

    @Override
    protected TOExecutionResult execution(File transformedAppFolder, TransformationContext transformationContext) {
        File fileToBeRemoved;
        try {
            fileToBeRemoved = getAbsoluteFile(transformedAppFolder, transformationContext);
        } catch (TransformationUtilityException e) {
            String result = String.format("No file has been removed because file path has not been resolved");
            return TOExecutionResult.noOp(this, result);
        }
        if(!fileToBeRemoved.exists()) {
            String result = String.format("File '%s' was not removed because it does not exist", getRelativePath());
            return TOExecutionResult.noOp(this, result);
        }

        TOExecutionResult result = null;
        try {
            boolean isDirectory = fileToBeRemoved.isDirectory();
            FileUtils.forceDelete(fileToBeRemoved);
            String details = String.format("%s '%s' has been removed", (isDirectory ? "Folder" : "File"), getRelativePath());
            result = TOExecutionResult.success(this, details);
        } catch (IOException e) {
            result = TOExecutionResult.error(this, e);
        }

        return result;
    }

    @Override
    public DeleteFile clone() throws CloneNotSupportedException {
        DeleteFile clonedDeleteFile = (DeleteFile) super.clone();
        return clonedDeleteFile;
    }

}