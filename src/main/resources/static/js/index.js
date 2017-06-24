ELEMENT.locale(ELEMENT.lang.en);
var vue = new Vue({
    el: '#app',
    mounted: function () {
        var _this = this;
        this.$nextTick(function () {
            $('#myflow').myflow({
                basePath: "static/img/48/",
                tools: {
                    save: function (data) {
                        $.ajax({
                            type: 'POST',
                            url: 'saveData',
                            data: {flow: JSON.stringify(data)},
                            success: function () {
                                window.location.href = 'download';
                            }
                        });
                    },
                    publish: function (data) {
                        console.log("发布", eval("(" + data + ")"));
                    },
                    addPath: function (id, data) {
                        //console.log("添加路径", id, eval("(" + data + ")"));
                    },

                    addRect: function (id, data) {
                        //console.log("添加状态",id,eval("("+data+")"));
                    },
                    clickPath: function (id) {
                        //console.log("点击线",id)
                    },
                    clickRect: function (id, data) {
                        //console.log("点击状态",id,eval("("+data+")"));
                    },
                    deletePath: function (id) {
                        //console.log("删除线",id);
                    },
                    deleteRect: function (id, data) {
                        //console.log("删除状态",id,eval("("+data+")"));
                    },
                    restoreSuccess: function () {
                        _this.fullscreenLoading = false;
                    },
                    revoke: function (id) {

                    }
                }
            });
        });
    },
    methods: {
        addIcon: function (node) {
            return 'lg-font icon-' + node;
        },
        handleClose: function (done) {
            this.activeName = 'property';
            this.closeLatestEdit();
            var name = null;
            var id = null;
            var currentNode = $(this.R).data('currNode');
            var _this = this;
            if (currentNode) {
                this.prop.propData.forEach(function (item) {
                    var key = item.key;
                    if (key === 'name') {
                        name = item.value.value;
                        currentNode.getProps().prop.name = name;
                    } else if (key === 'id') {
                        id = item.value.value;
                        //判断id是否存在
                        if (_this.rectIdSet.has(id)) {
                            //id已经存在
                            _this.idRepeat = true;
                        } else {
                            _this.rectIdSet.add(id);
                            _this.idRepeat = false;
                            currentNode.getProps().prop.id = id;
                            currentNode.setId(id);
                        }
                    } else {
                        var value = item.value.value;
                        currentNode.getProps().prop[key] = value;
                    }
                });
                var type = currentNode.getType();
                if (this.nodeMap[type].rule) {
                    currentNode.getProps().rule = [];
                    this.rule.ruleData.forEach(function (item) {
                        currentNode.getProps().rule.push({
                            index: item.index,
                            type: item.type,
                            fromNodeId: item.fromNodeId.value,
                            rule: item.rule.value
                        });
                    });
                }
                if (this.nodeMap[type].lan) {
                    currentNode.getProps().lan = [];
                    this.lan.lanData.forEach(function (item) {
                        currentNode.getProps().lan.push({
                            type: item.type,
                            language: item.language.value
                        });
                    });
                }
                $(this.R).trigger('textchange', [name, currentNode]);
                if (this.idRepeat) {
                    this.$message({
                        message: 'id repeat',
                        type: 'error',
                        duration: 2000
                    });
                } else {
                    done();
                }
            } else {
                this.$alert('Did not selected the node , the dialog is closed, please try again', 'warning', {
                    type: 'warning'
                });
                done();
            }
        },
        handleIconClick: function () {
            this.closeLatestEdit();
        },
        editProperty: function (row, column, cell, event) {
            this.closeLatestEdit();
            row[column.property].editFlag = true;
            this.latestCell = row[column.property];
            this.latestCellDom = $(event.target);
        },
        addRule: function () {
            this.closeLatestEdit();
            this.rule.ruleData.push({
                index: this.rule.ruleData.length + 1,
                type: 0,
                fromNodeId: {
                    value: '',
                    editFlag: false
                },
                rule: {
                    value: '',
                    editFlag: false
                }
            });
        },
        closeLatestEdit: function () {
            if (this.latestCell) {
                this.latestCell.editFlag = false;
            }

            if (this.latestCellDom) {
                var $dom = this.latestCellDom.find('.cell').length > 0 ? this.latestCellDom : this.latestCellDom.parent();
                var val = $dom.find('input').val();
                this.latestCell.value = val;
            }
        },
        deleteRule: function () {
            this.closeLatestEdit();
            var _this = this;
            this.$confirm('Are you sure you want to delete this record?', 'Tips', {
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                type: 'warning'
            }).then(function () {
                var index = _this.currentRow.index;
                _this.rule.ruleData.splice(index - 1, 1);
                _this.rule.ruleData.forEach(function (item, index) {
                    item.index = index + 1;
                })
            });
        },
        handleCurrentChange: function (val) {
            this.currentRow = val;
        },
        handleClick: function (tab) {
            this.closeLatestEdit();
        },
        beforeUpload: function (file) {
            this.fullscreenLoading = true;
        },
        uploadSuccess: function (res, file) {
            this.flow = res;
            $(this.R).trigger('restore');
        },
        uploadError: function (res, file) {
            this.$alert('Sorry, can not analyse the XML file, please check the format', 'warning', {
                type: 'warning'
            });
            this.fullscreenLoading = false;
        }
    },
    data: function () {
        return {
            nodeMap: {
                flowstart: {
                    prop: {
                        id: '',
                        name: '',
                        neVoicePath: '/zben/IVR/Cvoice/nepali/',
                        enVoicePath: '/zben/IVR/Cvoice/english/'
                    }
                },
                play: {
                    prop: {
                        id: '',
                        name: '',
                        breakFlag: '1',
                        remark: ''
                    },
                    rule: [],
                    lan: [{
                        type: '1',
                        language: ''
                    }, {
                        type: '2',
                        language: ''
                    }]
                },
                service: {
                    prop: {
                        id: '',
                        name: '',
                        className: '',
                        remark: ''
                    },
                    rule: [],
                },
                judge: {
                    prop: {
                        id: '',
                        name: '',
                        remark: ''
                    }
                },
                touchkey: {
                    prop: {
                        id: '',
                        name: '',
                        timeOut: '10',
                        finishkey: '',
                        repeatCount: '3',
                        length: '1',
                        className: '',
                        remark: ''
                    }
                },
                autooper: {
                    prop: {
                        id: '',
                        name: '',
                        autoOperNum: '',
                        remark: ''
                    }
                },
                handup: {
                    prop: {
                        id: '',
                        name: '',
                        reason: "2",
                        remark: ''
                    }
                }
            },//节点属性
            form: {
                toolTip: false
            },//设置功能
            latestCell: null, //最后编辑的列表对象
            latestCellDom: null, //最后编辑的dom对象
            prop: {
                show: true,
                propData: []
            }, //prop表格的值
            rule: {
                show: true,
                ruleData: [],
            }, //rule表格的值
            lan: {
                show: true,
                lanData: []
            }, //lan表格的值
            mode: true, //当前模式 true:select, false:path
            activeName: 'property', //当前激活的tab页
            currentRow: null, // rule表格选中的行对象
            dialogVisible: false, //控制属性弹出框的开关
            fullscreenLoading: false, //导入XML loading的开关
            rectIdSet: new Set(), //存放所有id的set容器
            idRepeat: false, //判断id时候重复
            R: null, //画布,
            flow: null
        }
    }
});