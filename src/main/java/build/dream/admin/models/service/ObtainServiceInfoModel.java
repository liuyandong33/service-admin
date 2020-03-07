package build.dream.admin.models.service;

import build.dream.common.models.weixinpay.DevOpsBasicModel;

import javax.validation.constraints.NotNull;

public class ObtainServiceInfoModel extends DevOpsBasicModel {
    @NotNull
    private Long serviceId;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }
}
