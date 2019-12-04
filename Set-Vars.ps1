<#
Copyright (c) 2018 Adam Spofford <aspofford.as@gmail.com>

Licensed under the Do What The Fuck You Want To Public License version 2 <http://www.wtfpl.net/about/>. This file may be copied,
modified, and redistributed in any way you can think of, as well as any that you can't.
#>

#Requires -Version 6.0

$values = @{}
$varfile = Get-Content ./vars.json | ConvertFrom-Json -AsHashTable
$vars = $varfile.vars
$keys = $vars.keys | Sort-Object { [int]($_.Substring(0, $_.IndexOf('/'))) }
foreach ($var in $keys)
{
    $defaultValue = $vars[$var]
    $var = $var.Substring($var.IndexOf('/') + 1)
    if ($defaultValue -ne '' -and $null -ne $defaultValue)
    {
        if ($defaultValue[0] -eq '$')
        {
            $defaultValue = $values[$defaultValue.Substring(1)]
        }
        $value = Read-Host -Prompt "Enter value for $var, or an empty line to set to $defaultValue"
        if ($value -eq '')
        {
            $values[$var] = $defaultValue
        }
        else
        {
            $values[$var] = $value
        }
    }
    else
    {
        do
        {
            $value = Read-Host -Prompt "Enter value for $var"
        } while ($value -eq '')
        $values[$var] = $value
    }
}
foreach ($movePath in $varfile.move)
{
    $templatePath = "$( $movePath )_TEMPLATE"
    if (Test-Path $movePath)
    {
        Remove-Item $movePath
    }
    Move-Item $templatePath $movePath
}
foreach ($fileLine in $varfile.files)
{
    foreach ($file in Get-ChildItem -Recurse -File $fileLine)
    {
        $content = Get-Content $file -Raw
        foreach ($value in $values.GetEnumerator())
        {
            $content = $content.Replace("PS_TEMPLATE_$( $value.Name )",$value.Value)
        }
        $content.Replace("PS_BUILTIN_YEAR", (Get-Date).Year) | Set-Content $file
    }
}
foreach ($copyItem in $varfile.copy.GetEnumerator())
{
    Copy-Item $copyItem.Name $copyItem.Value
}
Remove-Item ./vars.json
Remove-Item -LiteralPath $MyInvocation.MyCommand.Path -Force
