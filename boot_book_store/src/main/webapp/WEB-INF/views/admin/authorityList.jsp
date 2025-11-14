<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>권한 관리</title>

    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            background: #f2eee9;
            margin: 0;
            padding: 0;
        }

        .title {
            font-size: 32px;
            font-weight: 700;
            margin: 60px 0 26px 8%;
            color: #3e2c1c;
        }

        .table-container {
            width: 85%;
            margin: 0 auto;
            background: #ffffff;
            border-radius: 16px;
            padding: 10px 0 20px 0;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            font-size: 15px;
        }

        thead {
            background: #6b4f34;
            color: white;
        }

        th, td {
            padding: 14px 12px;
            text-align: center;
            border-bottom: 1px solid #ece4d9;
            color: #4b3b2a;
        }

        tbody tr:hover {
            background: #f8f5f1;
        }

        .role-select {
            padding: 7px 10px;
            border: 1px solid #d0c4b8;
            border-radius: 6px;
            background: #faf7f3;
            font-size: 14px;
            cursor: pointer;
        }

        .btn-save-role {
            padding: 7px 16px;
            background: #795438;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-size: 13px;
            transition: 0.2s;
        }

        .btn-save-role:hover {
            background: #8e6545;
        }
		.btn-remove-admin {
		    padding: 7px 14px;
		    background: #b43b3b;
		    color: #fff;
		    border: none;
		    border-radius: 6px;
		    cursor: pointer;
		    font-size: 13px;
		    transition: 0.2s;
		}

		.btn-remove-admin:hover {
		    background: #d14b4b;
		}
    </style>

</head>

<body>

<div class="title">권한 관리</div>

<div class="table-container">
    <table>
        <thead>
        <tr>
            <th>아이디</th>
            <th>이름</th>
            <th>현재 권한</th>
            <th>권한 변경</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="m" items="${members}">
            <tr>
                <td>${m.USER_ID}</td>
                <td>${m.USER_NAME}</td>

                <td>${m.USER_ROLE}</td>

                <td>
                    <select class="role-select" id="role_${m.USER_ID}">
                        <option value="USER"  ${m.USER_ROLE == 'USER'  ? 'selected' : ''}>일반</option>
                        <option value="ADMIN" ${m.USER_ROLE == 'ADMIN' ? 'selected' : ''}>관리자</option>
                    </select>

                    <button class="btn-save-role"
                            onclick="updateRole('${m.USER_ID}')">
                        변경
                    </button>
                </td>
            </tr>
        </c:forEach>
        </tbody>

    </table>
</div>
<br><br>
<div class="title" style="margin-top:40px;">현재 등록된 관리자 목록</div>

<div class="table-container">
    <table>
        <thead>
        <tr>
            <th>아이디</th>
            <th>이름</th>
            <th>이메일</th>
            <th>권한</th>
            <th>관리</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="a" items="${admins}">
            <tr>
                <td>${a.USER_ID}</td>
                <td>${a.USER_NAME}</td>
                <td>${a.USER_EMAIL}</td>
                <td>${a.USER_ROLE}</td>

                <td>
                    <button class="btn-remove-admin"
                            onclick="removeAdmin('${a.USER_ID}')">
                        관리자 제거
                    </button>
                </td>
            </tr>
        </c:forEach>
        </tbody>

    </table>
</div>


</body>
</html>
