/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sg.supersightings.viewmodels;

import com.sg.supersightings.models.Organization;
import java.util.List;

/**
 *
 * @author Thomas
 */
public class OrganizationVM {

    private Organization org;

    private List<Integer> memberIds;

    /**
     * @return the org
     */
    public Organization getOrg() {
        return org;
    }

    /**
     * @param org the org to set
     */
    public void setOrg(Organization org) {
        this.org = org;
    }

    /**
     * @return the memberIds
     */
    public List<Integer> getMemberIds() {
        return memberIds;
    }

    /**
     * @param memberIds the memberIds to set
     */
    public void setMemberIds(List<Integer> memberIds) {
        this.memberIds = memberIds;
    }

}
