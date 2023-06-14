package com.zjq.javacode.common;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CodeResponse extends BaseResponse {

    private RunResults results;

}
