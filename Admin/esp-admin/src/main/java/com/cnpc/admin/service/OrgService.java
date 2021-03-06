package com.cnpc.admin.service;

import com.cnpc.common.service.BaseService;
import com.cnpc.admin.entity.Org;
import com.cnpc.admin.mapper.OrgMapper;
import com.cnpc.common.util.LevelCodeUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class OrgService extends BaseService<OrgMapper,Org> {

    public List<Map> getTree(Org org) {
        List<Map> orgs = this.getOrgByPIDToMap(org.getParentid());
        return this.getTrees(orgs);
    }

    /**
     * 删除组织机构
     * @param id
     */
    public Boolean deleteById(String id) {
        //是否有子集的机构
        List<Map> maps = this.getOrgByPIDToMap(id);
        if (maps.size()>0){
            return false;
        }else {
            mapper.deleteByPrimaryKey(id);
            return true;
        }
    }

    /**
     * 根据一级节点获取树
     * @param orgs
     * @return
     */
    public List<Map> getTrees(List<Map> orgs){
        for (Map o: orgs ) {
            List<Map> orgByPID = this.getOrgByPIDToMap(o.get("id").toString());
            if (orgByPID.size()>0){
                o.put("children",orgByPID);
                getTrees(orgByPID);
            }
        }
        return  orgs;
    }
    /**
     * 根据父id，获取列表数据 返回List<map>
     * @param pid
     * @return
     */
    public List<Map> getOrgByPIDToMap(String pid){
        return mapper.getOrgByPIDToMap(pid);
    }

    public String getNextLevelCode(String parentId,String levelCode) {
        String lCode = mapper.getLevelCodeByParentId(parentId);
        return LevelCodeUtil.getNextLevelCode(levelCode,lCode);
    }

    public Boolean checkCode(String id, String code) {
        Org org = new Org();
        org.setCode(code);
        org.setId(id);
        List<Map> maps = mapper.checkCode(org);
        if (maps.size()>0){
            return false;
        }
        return true;
    }
}
