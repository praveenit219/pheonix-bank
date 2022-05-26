/*******************************************************************************
 * Copyright (c) Toppan Ecquaria Pte. Ltd.
 *
 * This program and the accompanying materials are made available under the terms
 * of the Toppan Ecquaria Pte. Ltd. Redistribution and use in source and binary
 * forms, with or without modification, are not permitted. Neither the name of
 * Toppan Ecquaria Pte. Ltd. or the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission and license agreements.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION)  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Contributors:
 *           Toppan Ecquaria (Bandung) Pte. Ltd.
 *           Toppan Ecquaria (Brunei) Sdn Bhd
 *           Toppan Ecquaria (Suzhou) Co Ltd.
 *           Toppan Ecquaria Pte. Ltd.
 *
 *  www.toppanecquaria.com
 ******************************************************************************/

package com.phoenix.phoenixbankapp.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {

    private String message;
    private String details;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String message, String details) {
        this.message = message;
        this.details = details;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

}
