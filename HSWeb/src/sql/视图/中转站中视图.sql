
/****** Object:  View [dbo].[V_DGMeter]    Script Date: 02/20/2014 10:52:46 ******/

create  or replace view V_DGMeter
as
select DG_Address,Meter_ID,Meter_Address,1   Meter_Style from AmMeter UNION ALL select DG_Address,Meter_ID,Meter_Address,2   Meter_Style from WaMeter

