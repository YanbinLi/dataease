package io.dataease.base.mapper.ext;

import io.dataease.base.domain.DatasetTableField;
import io.dataease.dto.LinkageInfoDTO;
import io.dataease.dto.PanelViewLinkageDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ExtPanelViewLinkageMapper {

    List<PanelViewLinkageDTO> getViewLinkageGather(@Param("panelId") String panelId,@Param("sourceViewId") String sourceViewId,@Param("targetViewIds") List<String> targetViewIds);

    List<LinkageInfoDTO> getPanelAllLinkageInfo(@Param("panelId") String panelId);

    List<DatasetTableField> queryTableField(@Param("table_id") String tableId);

    List<DatasetTableField> queryTableFieldWithViewId(@Param("viewId") String viewId);

    void deleteViewLinkage(@Param("panelId") String panelId,@Param("sourceViewId") String sourceViewId);

    void deleteViewLinkageField(@Param("panelId") String panelId,@Param("sourceViewId") String sourceViewId);

    void copyViewLinkage(@Param("copyId") String copyId);

    void copyViewLinkageField(@Param("copyId") String copyId);
}
